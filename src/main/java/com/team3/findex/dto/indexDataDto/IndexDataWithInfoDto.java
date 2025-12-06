package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;
import java.math.BigDecimal;

public record IndexDataWithInfoDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    BigDecimal currentPrice,
    BigDecimal beforePrice
) {

    public static IndexDataWithInfoDto fromIndexData(IndexData indexData) {
        return new IndexDataWithInfoDto(
            indexData.getIndexInfo().getId(),
            indexData.getIndexInfo().getIndexClassification(),
            indexData.getIndexInfo().getIndexName(),
            indexData.getVersus(),
            indexData.getFluctuationRate(),
            indexData.getMarketPrice(), //?? 시가??
            indexData.getClosingPrice() //?? 종가??
        );
    }
}
