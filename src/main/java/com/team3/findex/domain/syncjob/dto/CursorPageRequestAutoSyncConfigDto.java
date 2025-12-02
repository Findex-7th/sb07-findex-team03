package com.team3.findex.domain.syncjob.dto;

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
