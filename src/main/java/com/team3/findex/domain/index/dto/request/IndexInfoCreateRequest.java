package com.team3.findex.domain.index.dto.request;

import com.team3.findex.domain.index.SourceType;

import java.time.LocalDate;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    String basePointInTime,
    Double baseIndex,
    Boolean favorite,
    SourceType sourceType
) {}
