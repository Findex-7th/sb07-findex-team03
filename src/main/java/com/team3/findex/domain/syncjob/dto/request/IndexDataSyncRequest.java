package com.team3.findex.domain.syncjob.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "지수 데이터 동기화 요청 DTO")
public record IndexDataSyncRequest(
        @Schema(description = "동기화할 지수 정보 ID 목록", example = "[1, 2, 3]")
        List<Long> indexInfoIds,

        @Schema(description = "동기화 시작 날짜 (YYYY-MM-DD)", example = "2023-01-01")
        String baseDateFrom,

        @Schema(description = "동기화 종료 날짜 (YYYY-MM-DD)", example = "2023-12-31")
        String baseDateTo
) {
}
