package com.kafkabroker.kafkabroker.schedulers;

import com.kafkabroker.kafkabroker.feignclients.DistantWorkTime;
import com.kafkabroker.kafkabroker.models.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SchedulerReport
{
    private final DistantWorkTime distantWorkTime;

    @Scheduled(cron = "${scheduled.cron.expression}")
    @Async
    public void createReport()
    {
        List<Employee> employeeList = distantWorkTime.readEmployees().getBody();

        


    }
}
