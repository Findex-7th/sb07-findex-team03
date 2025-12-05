package com.team3.findex.dto.indexDataDto;

import java.util.List;

public record IndexChartDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    String periodType,     // MONTHLY, QUARTERLY, YEARLY
    List<ChartDataPointDto> dataPoints,
    List<ChartDataPointDto> ma5DataPoints,
    List<ChartDataPointDto> ma20DataPoints
) {}