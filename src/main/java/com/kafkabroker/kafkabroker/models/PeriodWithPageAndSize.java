package com.kafkabroker.kafkabroker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class PeriodWithPageAndSize
{
    @NonNull
    private List<PeriodWithAllIds> periodList;
    private Integer page;
    private Integer size;
    private Integer totalPages;
}
