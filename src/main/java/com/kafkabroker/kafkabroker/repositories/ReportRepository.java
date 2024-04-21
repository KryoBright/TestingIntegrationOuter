package com.kafkabroker.kafkabroker.repositories;

import com.kafkabroker.kafkabroker.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String>
{

}
