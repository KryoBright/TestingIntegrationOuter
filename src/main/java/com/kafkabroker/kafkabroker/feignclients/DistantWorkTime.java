package com.kafkabroker.kafkabroker.feignclients;

import com.kafkabroker.kafkabroker.models.Employee;
import com.kafkabroker.kafkabroker.models.FilterAndSorting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "worktime", url = "${client.clientName.url}")
public interface DistantWorkTime
{
    @GetMapping("/employee/all")
    ResponseEntity<List<Employee>> readEmployees();

    @GetMapping("/period/all")
    Object readPeriods(
            @RequestBody FilterAndSorting filterAndSorting
    );

    @GetMapping("/slot/{id}")
    public Object readSlot(
            @PathVariable String id
    );
}
