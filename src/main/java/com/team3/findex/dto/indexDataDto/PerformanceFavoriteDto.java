package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;
import java.math.BigDecimal;

public record PerformanceFavoriteDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    BigDecimal currentPrice) {
    public static PerformanceFavoriteDto from(IndexData indexData) {
        return new PerformanceFavoriteDto(
            indexData.getIndexInfo().getId(),
            indexData.getIndexInfo().getIndexClassification(),
            indexData.getIndexInfo().getIndexName(),
            indexData.getVersus(),
            indexData.getFluctuationRate(),
            indexData.getClosingPrice()
        );
    }
}
