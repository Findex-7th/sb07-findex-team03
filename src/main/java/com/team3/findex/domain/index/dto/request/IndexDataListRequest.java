package com.team3.findex.domain.index.dto.request;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public record IndexDataListRequest(

    @NotNull(message = "🚨indexInfoId 필수입니다.")
    Long indexInfoId,

    LocalDate startDate,
    LocalDate endDate,
    Long idAfter,
    String cursor,

    @Pattern(
        regexp = "baseDate|marketPrice|closingPrice|highPrice|lowPrice|versus|fluctuationRate|tradingQuantity|tradingPrice|marketTotalAmount",
        message = "지원되지 않는 정렬 필드입니다."
    )
    String sortField,

    @Pattern(regexp = "asc|desc", message = "sortDirection must be 'asc' or 'desc'")
    String sortDirection,
    Integer size
) {}
