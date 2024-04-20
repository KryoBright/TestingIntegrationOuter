package com.kafkabroker.kafkabroker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class FilterAndSorting
{
    private Filter filter;
    private Sort sort;
    private Integer page;
    private Integer size;
}
