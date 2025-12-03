package com.team3.findex.dto.indexDataDto;

import java.util.List;

public record IndexChartDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    String periodType,     // MONTHLY, QUARTERLY, YEARLY
    List<ChartDataPoint> dataPoints,
    List<ChartDataPoint> ma5DataPoints,
    List<ChartDataPoint> ma20DataPoints
) {}