package com.team3.findex.dto.indexDataDto;

public record RankedIndexPerformanceDto(
    IndexDataWithInfoDto performance,
    Integer rank
) {}
