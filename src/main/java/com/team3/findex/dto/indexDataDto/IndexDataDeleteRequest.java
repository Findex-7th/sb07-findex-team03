package com.team3.findex.dto.indexDataDto;

import jakarta.validation.constraints.NotNull;

public record IndexDataDeleteRequest(
    @NotNull Long indexInfoId
) { }
