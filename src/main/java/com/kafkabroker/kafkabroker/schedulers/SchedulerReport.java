package com.kafkabroker.kafkabroker.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafkabroker.kafkabroker.feignclients.DistantWorkTime;
import com.kafkabroker.kafkabroker.kafka.KafkaProducer;
import com.kafkabroker.kafkabroker.models.*;
import com.kafkabroker.kafkabroker.repositories.ReportRepository;
import com.kafkabroker.kafkabroker.repositories.WorkEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SchedulerReport
{
    private final DistantWorkTime distantWorkTime;

    private final ReportRepository reportRepository;
    private final WorkEmployeeRepository workEmployeeRepository;

    private final KafkaProducer kafkaProducer;

    @Scheduled(cron = "${scheduled.cron.expression}")
    @Async
    public void createReport() throws JsonProcessingException {
        List<Employee> employeeList = distantWorkTime.readEmployees().getBody();

        if (employeeList != null)
        {
            List<WorkEmployee> workEmployeeList = new ArrayList<>();

            List<Report> lastReportList = reportRepository.findAll(Sort.by(Sort.Order.desc("beginTime")));
            Report lastReport = null;

            if (lastReportList.size() > 0)
            {
                lastReport = lastReportList.get(0);
            }

            if (lastReport == null)
            {
                LocalDateTime earliestTime = LocalDateTime.MAX;

                for (int i = 0; i < employeeList.size(); i++)
                {
                    PeriodWithPageAndSize periodWithPageAndSize = distantWorkTime
                            .readPeriods(FilterAndSorting
                                    .builder()
                                    .filter(Filter
                                            .builder()
                                            .executorId(employeeList
                                                    .get(i)
                                                    .getId())
                                            .build())
                                    .build());

                    long numberOfHours = 0;

                    if (!periodWithPageAndSize.getPeriodList().isEmpty())
                    {
                        for (int j = 0; j < periodWithPageAndSize.getPeriodList().size(); j++)
                        {
                            SlotWithScheduleTemplateId slot = distantWorkTime
                                    .readSlot(periodWithPageAndSize
                                            .getPeriodList()
                                            .get(j)
                                            .getSlotId());

                            numberOfHours += Math.abs(Duration.between(slot.getBeginTime(), slot.getEndTime()).toHours());

                            if (slot.getEndTime().isBefore(earliestTime))
                            {
                                earliestTime = slot.getEndTime();
                            }
                        }
                    }

                    WorkEmployee workEmployee = WorkEmployee
                            .builder()
                            .hours(numberOfHours)
                            .salary(numberOfHours*salaryCoefficient(employeeList
                                    .get(i)
                                    .getStatus()))
                            .build();

                    workEmployeeList.add(workEmployee);
                }

                Report newReport = Report.builder().beginTime(earliestTime).endTime(LocalDateTime.now()).employees(workEmployeeList).build();

                for (WorkEmployee workEmployee : workEmployeeList) {
                    workEmployee.setReport(newReport);
                    workEmployeeRepository.save(workEmployee);
                }

                reportRepository.save(newReport);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String jsonString = objectMapper.writeValueAsString(newReport);

                kafkaProducer.sendMessage("employee.service.reports", jsonString);

            }
            else
            {
                //
                for (int i = 0; i < employeeList.size(); i++)
                {
                    PeriodWithPageAndSize periodWithPageAndSize = distantWorkTime
                            .readPeriods(FilterAndSorting
                                    .builder()
                                    .filter(Filter
                                            .builder()
                                            .executorId(employeeList
                                                    .get(i)
                                                    .getId())
                                            .beginTime(lastReport
                                                    .getEndTime().atZone(ZoneId.of("Asia/Tomsk")))
                                            .build())
                                    .build());

                    long numberOfHours = 0;

                    if (!periodWithPageAndSize.getPeriodList().isEmpty())
                    {
                        for (int j = 0; j < periodWithPageAndSize.getPeriodList().size(); j++)
                        {
                            SlotWithScheduleTemplateId slot = distantWorkTime
                                    .readSlot(periodWithPageAndSize
                                            .getPeriodList()
                                            .get(j)
                                            .getSlotId());

                            numberOfHours += Duration.between(slot.getBeginTime(), slot.getEndTime()).toHours();
                        }
                    }

                    WorkEmployee workEmployee = WorkEmployee
                            .builder()
                            .hours(numberOfHours)
                            .salary(numberOfHours*salaryCoefficient(employeeList
                                    .get(i)
                                    .getStatus()))
                            .build();



                    workEmployeeList.add(workEmployee);
                }

                Report newReport = Report.builder().beginTime(lastReport.getEndTime()).endTime(LocalDateTime.now()).employees(workEmployeeList).build();

                for (WorkEmployee workEmployee : workEmployeeList) {
                    workEmployee.setReport(newReport);
                    workEmployeeRepository.save(workEmployee);
                }

                reportRepository.save(newReport);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String jsonString = objectMapper.writeValueAsString(newReport);

                kafkaProducer.sendMessage("employee.service.reports", jsonString);
            }


        }
        else
        {
            Report newReport = Report.builder().endTime(LocalDateTime.now()).beginTime(LocalDateTime.now()).employees(null).build();
            reportRepository.save(newReport);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonString = objectMapper.writeValueAsString(newReport);

            kafkaProducer.sendMessage("employee.service.reports", jsonString);
        }

    }

    public double salaryCoefficient(Status status)
    {
        return switch (status) {
            case DISMISSED -> 0;
            case TIME_OFF -> 300;
            case TRIAL -> 500;
            case WORKING -> 1000;
        };
    }
}
