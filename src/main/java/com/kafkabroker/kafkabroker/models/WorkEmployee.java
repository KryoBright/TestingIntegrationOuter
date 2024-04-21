package com.kafkabroker.kafkabroker.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "work_employee")
public class WorkEmployee
{
    @Id
    @Column(length = 32)
    private String id;

    private long hours;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @PrePersist
    private void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
    }
}
