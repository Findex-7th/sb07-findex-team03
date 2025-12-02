package com.team3.findex.dto.indexDataDto;

import java.util.List;
import jakarta.validation.constraints.NotNull;

public record CursorPageResponse<T>(
    @NotNull
    String nextCursor,
    @NotNull
    String nextIdAfter,

    int size,
    long totalElements,
    boolean hasNext,

    @NotNull
    List<T> content
) {}
