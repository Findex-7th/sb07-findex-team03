package com.team3.findex.dto.indexDataDto;

public record RankedIndexPerformanceDto(
    IndexPerformanceDto performance,
    Integer rank
) {}
