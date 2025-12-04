package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;

public record IndexPerformanceDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    Double versus,
    Double fluctuationRate,
    Double currentPrice,
    Double beforePrice
) {

    public static IndexPerformanceDto fromIndexData(IndexData indexData) {
        return new IndexPerformanceDto(
            indexData.getIndexInfo().getId(),
            indexData.getIndexInfo().getIndexClassification(),
            indexData.getIndexInfo().getIndexName(),
            indexData.getVersus().doubleValue(),
            indexData.getFluctuationRate().doubleValue(),
            indexData.getMarketPrice().doubleValue(), //?? 시가??
            indexData.getClosingPrice().doubleValue() //?? 종가??
        );
    }
    public static IndexPerformanceDto fromFavoriteDto(PerformanceFavoriteDto favoriteDto) {
        return new IndexPerformanceDto(
            favoriteDto.indexInfoId(),
            favoriteDto.indexClassification(),
            favoriteDto.indexName(),
            favoriteDto.versus(),
            favoriteDto.fluctuationRate(),
            favoriteDto.currentPrice(),
            favoriteDto.currentPrice() - favoriteDto.versus()
        );
    }
}
