package com.team3.findex.domain.syncjob.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "연동 이력 Cursor 페이징 조회 응답 DTO")
public record CursorPageResponseSyncJobDto(
        @Schema(description = "조회된 연동 이력 데이터 목록")
        List<SyncJobDto> content,

        @Schema(description = "다음 페이지 조회를 위한 커서 값 (정렬 기준 값)", example = "2023-12-25")
        String nextCursor,

        @Schema(description = "다음 페이지 조회를 위한 기준 ID (커서 값이 같을 경우 식별용)", example = "105")
        Long nextIdAfter,

        @Schema(description = "페이지 크기 (요청받은 size)", example = "10")
        int size,

        @Schema(description = "전체 데이터 개수 (필터링 된 전체 건수)", example = "150")
        Long totalElements,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
}
