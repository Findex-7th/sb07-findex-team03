package com.team3.findex.domain.index.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record IndexDataCreateRequest(
        Long indexInfoId,
        String baseDate,
        BigDecimal marketPrice,
        BigDecimal closingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        BigDecimal tradingQuantity,
        BigDecimal tradingPrice,
        BigDecimal marketTotalAmount
) {}
