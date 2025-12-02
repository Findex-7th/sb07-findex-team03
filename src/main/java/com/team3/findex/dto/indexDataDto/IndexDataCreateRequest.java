package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.*;

public record IndexDataCreateRequest(
    @NotNull Long indexInfoId,
    @NotBlank String baseDate,
    @NotNull Double marketPrice,
    @NotNull Double closingPrice,
    @NotNull Double highPrice,
    @NotNull Double lowPrice,
    @NotNull Double versus,
    @NotNull Double fluctuationRate,
    @NotNull Long tradingQuantity,
    @NotNull Long tradingPrice,
    @NotNull Long marketTotalAmount
) {}
