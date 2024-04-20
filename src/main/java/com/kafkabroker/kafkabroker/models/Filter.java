package com.kafkabroker.kafkabroker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class Filter
{
    private String id;
    private String slotId;
    private String scheduleId;
    private SlotType slotType;
    private String administratorId;
    private String executorId;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
}
