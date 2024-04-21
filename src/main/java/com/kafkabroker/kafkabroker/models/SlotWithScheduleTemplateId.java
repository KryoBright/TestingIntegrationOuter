package com.kafkabroker.kafkabroker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class SlotWithScheduleTemplateId
{
    @NonNull
    private String scheduleTemplateId;
    @NonNull
    private LocalDateTime beginTime;
    @NonNull
    private LocalDateTime endTime;
}
