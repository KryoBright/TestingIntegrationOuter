package com.kafkabroker.kafkabroker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class PeriodWithAllIds
{
    @NonNull
    private String id;
    @NonNull
    private String slotId;
    @NonNull
    private String administratorId;
    @NonNull
    private String scheduleId;
    @NonNull
    private SlotType slotType;
    private String executorId;
}
