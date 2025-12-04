package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.NotNull;

public record IndexDataDeleteRequest(
    @NotNull(message = "🚨indexInfoId 필수입니다.") Long indexInfoId
) { }
