package com.team3.findex.domain.autosync.dto;

import java.util.List;

public record CursorPageResponseAutoSyncConfigDto(
        List<AutoSyncConfigDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        Long totalElements,
        boolean hasNext
) {
}
