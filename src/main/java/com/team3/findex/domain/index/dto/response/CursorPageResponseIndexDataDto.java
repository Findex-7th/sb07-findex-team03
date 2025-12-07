package com.team3.findex.domain.index.dto.response;

import com.team3.findex.common.util.CursorEncodingUtil;
import com.team3.findex.dto.indexDataDto.IndexDataDto;

import java.util.List;

public record CursorPageResponseIndexDataDto(
        List<IndexDataDto> content,
        String nextCursor,
        String nextIdAfter,
        int size,
        Long totalElements,
        boolean hasNext
) {
}
