package com.team3.findex.domain.index.dto;

import com.team3.findex.domain.index.enums.ChartPeriodType;

import java.util.List;

public record IndexChartDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    ChartPeriodType periodType,     // MONTHLY, QUARTERLY, YEARLY
    List<ChartDataPointDto> dataPoints,
    List<ChartDataPointDto> ma5DataPoints,
    List<ChartDataPointDto> ma20DataPoints
) {
}