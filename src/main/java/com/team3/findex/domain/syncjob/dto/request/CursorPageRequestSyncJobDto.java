package com.team3.findex.domain.syncjob.dto.request;

import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "연동 이력 Cursor 페이징 조회 요청 DTO")
public record CursorPageRequestSyncJobDto(

        @Schema(description = "작업 유형 필터 (INDEX_INFO: 지수정보, INDEX_DATA: 지수데이터)", example = "INDEX_DATA")
        JobType jobType,

        @Schema(description = "특정 지수 정보 ID로 필터링", example = "1")
        Long indexInfoId,

        @Schema(description = "기준 일자 시작 날짜 (TargetDate 기준, YYYY-MM-DD)", example = "2023-01-01")
        String baseDateFrom,

        @Schema(description = "기준 일자 종료 날짜 (TargetDate 기준, YYYY-MM-DD)", example = "2023-12-31")
        String baseDateTo,

        @Schema(description = "작업 수행자(IP)로 필터링", example = "127.0.0.1")
        String worker,

        @Schema(description = "작업 생성 시간(JobTime) 시작 필터", example = "2024-01-01T00:00:00Z")
        String jobTimeFrom,

        @Schema(description = "작업 생성 시간(JobTime) 종료 필터", example = "2025-12-07T23:59:59Z")
        String jobTimeTo,

        @Schema(description = "작업 결과 상태 (SUCCESS, FAILED)", allowableValues = {"SUCCESS", "FAILED"}, example = "SUCCESS")
        String status,

        @Schema(description = "커서 페이징: 이전 페이지 마지막 항목의 ID (중복 커서 값 구분용)", example = "105")
        Long idAfter,

        @Schema(description = "커서 페이징: 이전 페이지 마지막 항목의 커서 값 (정렬 기준 값)", example = "2023-12-25")
        String cursor,

        @Schema(description = "정렬 기준 필드 (targetDate, jobTime)", allowableValues = {"targetDate", "jobTime"}, example = "jobTime")
        String sortField,

        @Schema(description = "정렬 방향 (ASC, DESC)", allowableValues = {"ASC", "DESC"}, example = "DESC" )
        String sortDirection,

        @Schema(description = "페이지 당 데이터 개수", example = "10")
        int size
) {
}
