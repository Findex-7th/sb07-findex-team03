package com.team3.findex.domain.index.dto.response;

import java.util.List;

public record CursorPageResponseIndexInfoDto(
    List<IndexInfoDto> contents,
    Long nextCursor,
    boolean hasNext
) {}
