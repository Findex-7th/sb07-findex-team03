package com.team3.findex.domain.index.dto.request;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    String basePointInTime,
    Double baseIndex,
    Boolean favorite
) {}
