package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record GetChartDataRequest(

    @NotNull
    @Min(1)
    Long id,  // 지수 정보 ID

    @Pattern(
        regexp = "DAILY|MONTHLY|QUARTERLY|YEARLY",
        message = "periodType must be one of DAILY, MONTHLY, QUARTERLY, YEARLY"
    )
    String periodType  // 차트 기간 유형, default: DAILY
) {}