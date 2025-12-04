package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;

public record PerformanceFavoriteDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    Double versus,
    Double fluctuationRate,
    Double currentPrice) {
    public static PerformanceFavoriteDto from(IndexData indexData) {
        return new PerformanceFavoriteDto(
            indexData.getIndexInfo().getId(),
            indexData.getIndexInfo().getIndexClassification(),
            indexData.getIndexInfo().getIndexName(),
            indexData.getVersus().doubleValue(),
            indexData.getFluctuationRate().doubleValue(),
            indexData.getClosingPrice().doubleValue()
        );
    }
}
