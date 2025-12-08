package com.team3.findex.domain.index.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * IndexData 검색 조건 DTO
 * <p>
 * 지수 선택 및 기간으로 데이터를 필터링합니다.
 * </p>
 */
public record IndexDataFindCondition(
        @Schema(description = "지수(완전 일치)")
        Long indexInfoId,

        @Schema(description = "시작 날짜 (YYYY-MM-DD)", example = "2024-01-01")
        LocalDate startDate,

        @Schema(description = "종료 날짜 (YYYY-MM-DD)", example = "2024-12-31")
        LocalDate endDate
) {
}
