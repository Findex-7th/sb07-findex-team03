package com.team3.findex.domain.autosync.dto;

import java.util.List;

/**
 * 커서 기반 페이지 응답 DTO
 */
public record CursorPageResponseAutoSyncConfigDto(
        List<AutoSyncConfigDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        Long totalElements,
        boolean hasNext
) {
}
