package com.team3.findex.domain.index.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record IndexInfoFindCondition(
        @Schema(description = "지수 분류 (부분 일치 검색)")
        String indexClassification,

        @Schema(description = "지수 이름 (부분 일치 검색)")
        String indexName,

        @Schema(description = "즐겨찾기 여부 (null: 전체, true: 즐겨찾기만, false: 비즐겨찾기만)")
        Boolean isFavorite
) {
}
