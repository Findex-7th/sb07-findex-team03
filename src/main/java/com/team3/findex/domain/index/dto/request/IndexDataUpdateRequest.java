package com.team3.findex.domain.index.dto.request;

import jakarta.validation.constraints.NotNull;

public record IndexDataUpdateRequest(

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