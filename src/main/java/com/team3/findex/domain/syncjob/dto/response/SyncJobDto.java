package com.team3.findex.domain.syncjob.dto.response;


import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDateTime;

@Schema(description = "연동 이력 Cursor 조회 응답 DTO 내부 Content값")
public record SyncJobDto(
        @Schema(description = "작업 ID", example = "10")
        Long id,

        @Schema(description = "작업 유형", example = "INDEX_DATA")
        JobType jobType,

        @Schema(description = "관련 지수 ID", example = "1")
        Long indexInfoId,

        @Schema(description = "대상 날짜 (YYYY-MM-DD)", example = "2023-12-25")
        String targetDate,

        @Schema(description = "작업 수행자(IP 등)", example = "127.0.0.1")
        String worker,

        @Schema(description = "작업 수행 시간", example = "2023-12-25T10:00:00Z")
        Instant jobTime,

        @Schema(description = "작업 결과", example = "SUCCESS")
        Result result
) {
}
