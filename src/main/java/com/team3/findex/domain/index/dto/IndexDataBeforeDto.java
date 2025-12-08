package com.team3.findex.domain.index.dto;

public record IndexDataBeforeDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    Double versusAvg,
    Double fluctuationRateAvg,
    Double closingPriceAvg
) {}
