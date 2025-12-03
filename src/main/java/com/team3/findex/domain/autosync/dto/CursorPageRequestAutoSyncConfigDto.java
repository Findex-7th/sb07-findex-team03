package com.team3.findex.domain.autosync.dto;

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
