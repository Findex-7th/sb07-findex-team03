package com.team3.findex.synclog.dto;

public record CursorPageRequestAutoSyncConfigDto(
        Long indexInfoId,
        boolean enabled,
        Long idAfter,
        String cursor,
        String sortField,
        String sortDirection,
        int size
) {
}
