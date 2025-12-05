package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record IndexDataCreateRequest(
    @NotNull(message = "🚨indexInfoId 필수입니다.") Long indexInfoId,
    @NotBlank(message = "🚨baseDate is @NotBlank") String baseDate,
//??    @NotBlank String sourceType,
    @NotNull(message = "🚨marketPrice 필수입니다.") BigDecimal marketPrice,
    @NotNull(message = "🚨closingPrice 필수입니다.") BigDecimal closingPrice,
    @NotNull(message = "🚨highPrice 필수입니다.") BigDecimal highPrice,
    @NotNull(message = "🚨lowPrice 필수입니다.") BigDecimal lowPrice,
    @NotNull(message = "🚨versus 필수입니다.") BigDecimal versus,
    @NotNull(message = "🚨fluctuationRate 필수입니다.") BigDecimal fluctuationRate,
    @NotNull(message = "🚨tradingQuantity 필수입니다.") BigDecimal tradingQuantity,
    @NotNull(message = "🚨tradingPrice 필수입니다.") BigDecimal tradingPrice,
    @NotNull(message = "🚨marketTotalAmount 필수입니다.") BigDecimal marketTotalAmount
) {}
