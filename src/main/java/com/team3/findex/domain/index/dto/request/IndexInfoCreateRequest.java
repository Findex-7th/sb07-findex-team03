package com.team3.findex.domain.index.dto.request;

import java.time.LocalDate;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    Double baseIndex,
    Boolean favorite
) {}
