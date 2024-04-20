package com.kafkabroker.kafkabroker.models;

import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee
{
    private String id;

    private String employeeName;
    private Status status;
}
