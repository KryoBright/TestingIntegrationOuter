package com.kafkabroker.kafkabroker.repositories;

import com.kafkabroker.kafkabroker.models.WorkEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkEmployeeRepository extends JpaRepository<WorkEmployee, String> {
}
