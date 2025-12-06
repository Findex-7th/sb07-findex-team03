package com.team3.findex.domain.index.dto.response;

import java.util.List;

public record CursorPageResponseIndexInfoDto(
    List<IndexInfoDto> content,
    String nextCursor,
    String newtIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {}
