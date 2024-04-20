package com.kafkabroker.kafkabroker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class Sort
{
    private Field field;
    private Direction direction;
}
