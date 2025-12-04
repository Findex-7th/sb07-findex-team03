package com.team3.findex.dto.indexDataDto;

import java.util.List;
import jakarta.validation.constraints.NotNull;

public record CursorPageResponse<T>(
    @NotNull(message = "🚨 필수입니다.")
    String nextCursor,
    @NotNull(message = "🚨 필수입니다.")
    String nextIdAfter,

    int size,
    long totalElements,
    boolean hasNext,

    @NotNull(message = "🚨 필수입니다.")
    List<T> content
) {}
