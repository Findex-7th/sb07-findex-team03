package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public record ExportCsvRequest(

    @NotNull
    @Min(1)
    Long indexInfoId,  // 지수 정보 ID

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,  // 시작 일자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,  // 종료 일자

    @Pattern(
        regexp = "baseDate|marketPrice|closingPrice|highPrice|lowPrice|versus|fluctuationRate|tradingQuantity|tradingPrice|marketTotalAmount",
        message = "sortField must be one of baseDate, marketPrice, closingPrice, highPrice, lowPrice, versus, fluctuationRate, tradingQuantity, tradingPrice, marketTotalAmount"
    )
    String sortField,  // 정렬 필드, default: baseDate

    @Pattern(regexp = "asc|desc", message = "sortDirection must be 'asc' or 'desc'")
    String sortDirection  // 정렬 방향, default: desc
) {}
