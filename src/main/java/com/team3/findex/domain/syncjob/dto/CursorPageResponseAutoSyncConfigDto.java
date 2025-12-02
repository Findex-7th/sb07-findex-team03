package com.team3.findex.domain.syncjob.dto;

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
