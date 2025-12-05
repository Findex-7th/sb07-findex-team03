package com.team3.findex.domain.index.dto.response;
import com.team3.findex.domain.index.SourceType;
import java.time.LocalDate;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    String basePointInTime,
    Double baseIndex,
    SourceType sourceType,
    Boolean favorite
) {}
