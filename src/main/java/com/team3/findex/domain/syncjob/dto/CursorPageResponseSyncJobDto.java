package com.team3.findex.domain.syncjob.dto;

import java.util.List;

public record CursorPageResponseSyncJobDto(
        List<SyncJobDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        Long totalElements,
        boolean hasNext
) {
}
