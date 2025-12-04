package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.*;

public record IndexDataCreateRequest(
    @NotNull(message = "🚨indexInfoId 필수입니다.") Long indexInfoId,
    @NotBlank(message = "🚨baseDate is @NotBlank") String baseDate,
//??    @NotBlank String sourceType,
    @NotNull(message = "🚨marketPrice 필수입니다.") Double marketPrice,
    @NotNull(message = "🚨closingPrice 필수입니다.") Double closingPrice,
    @NotNull(message = "🚨highPrice 필수입니다.") Double highPrice,
    @NotNull(message = "🚨lowPrice 필수입니다.") Double lowPrice,
    @NotNull(message = "🚨versus 필수입니다.") Double versus,
    @NotNull(message = "🚨fluctuationRate 필수입니다.") Double fluctuationRate,
    @NotNull(message = "🚨tradingQuantity 필수입니다.") Long tradingQuantity,
    @NotNull(message = "🚨tradingPrice 필수입니다.") Long tradingPrice,
    @NotNull(message = "🚨marketTotalAmount 필수입니다.") Long marketTotalAmount
) {}
