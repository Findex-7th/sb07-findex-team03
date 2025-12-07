package com.team3.findex.domain.index.dto.response;

import com.team3.findex.domain.index.dto.IndexDataDto;

import java.util.List;

public record CursorPageResponseIndexDataDto(
        List<IndexDataDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        Long totalElements,
        boolean hasNext
) {
}
