package com.team3.findex.domain.autosync.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CursorPageRequestAutoSyncConfigDto(
        @Schema(description = "지수 정보 ID")
        Long indexInfoId,

        @Schema(description = "활성화 여부")
        boolean enabled,

        @Schema(description = "이전 페이지 마지막 요소 ID")
        Long idAfter,

        @Schema(description = "커서 (다음 페이지 시작점)")
        String cursor,

        @Schema(description = "정렬 필드 (indexInfo.indexName, enabled)",
                defaultValue = "indexInfo.indexName")
        String sortField,

        @Schema(description = "정렬 방향 (asc, desc)",
                defaultValue = "asc")
        String sortDirection,

        @Schema(description = "페이지 크기", defaultValue = "10")
        int size
) {
}
