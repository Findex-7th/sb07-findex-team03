package com.team3.findex.domain.index.dto;

public record RankedIndexPerformanceDto(
    IndexDataWithInfoDto performance,
    Integer rank
) {}
