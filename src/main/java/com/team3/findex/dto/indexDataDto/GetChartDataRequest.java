package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.PeriodType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record GetChartDataRequest(

    @NotNull(message = "🚨id 필수입니다.")
    @Min(1)
    Long id,  // 지수 정보 ID

    PeriodType periodType  // 차트 기간 유형, default: DAILY
) {}