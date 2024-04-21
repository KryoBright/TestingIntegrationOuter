package com.kafkabroker.kafkabroker.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report")
public class Report
{
    @Id
    @Column(length = 32)
    private String id;

    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "report")
    private List<WorkEmployee> employees;

    @PrePersist
    private void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
    }
}
