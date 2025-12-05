package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;
import java.math.BigDecimal;

public record IndexPerformanceDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    BigDecimal currentPrice,
    BigDecimal beforePrice
) {

    public static IndexPerformanceDto fromIndexData(IndexData indexData) {
        return new IndexPerformanceDto(
            indexData.getIndexInfo().getId(),
            indexData.getIndexInfo().getIndexClassification(),
            indexData.getIndexInfo().getIndexName(),
            indexData.getVersus(),
            indexData.getFluctuationRate(),
            indexData.getMarketPrice(), //?? 시가??
            indexData.getClosingPrice() //?? 종가??
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
            favoriteDto.currentPrice().subtract(favoriteDto.versus())
        );
    }
}
