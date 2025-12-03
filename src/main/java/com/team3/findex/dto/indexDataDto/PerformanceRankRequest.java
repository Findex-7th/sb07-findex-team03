package com.team3.findex.dto.indexDataRequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

public record PerformanceRankRequest(

    @NotNull(message = "🚨indexInfoId 필수입니다.")
    @Min(1)
    Long indexInfoId,  // 지수 정보 ID

    @Pattern(
        regexp = "DAILY|WEEKLY|MONTHLY",
        message = "periodType must be one of DAILY, WEEKLY, MONTHLY"
    )
    String periodType,  // 성과 기간 유형, default: DAILY

    @Min(1)
    Integer limit  // 조회 개수 제한
) {}
