package com.team3.findex.dto.indexDataDto;

public record IndexDataDto(
    Long id,
    Long indexInfoId,
    String baseDate,
    String sourceType,      // USER, OPEN_API
    Double marketPrice,
    Double closingPrice,
    Double highPrice,
    Double lowPrice,
    Double versus,
    Double fluctuationRate,
    Long tradingQuantity,
    Long tradingPrice,
    Long marketTotalAmount
) {}
