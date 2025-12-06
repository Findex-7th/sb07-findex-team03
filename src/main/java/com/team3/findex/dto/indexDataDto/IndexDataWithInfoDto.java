package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;
import java.math.BigDecimal;
import lombok.Setter;

public record IndexDataWithInfoDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    BigDecimal currentPrice,

    @Setter
    BigDecimal beforePrice
) {

    public static IndexDataWithInfoDto fromIndexData(IndexData indexData) {
        return new IndexDataWithInfoDto(
            indexData.getIndexInfo().getId(),
            indexData.getIndexInfo().getIndexClassification(),
            indexData.getIndexInfo().getIndexName(),
            indexData.getVersus(),
            indexData.getFluctuationRate(),
            indexData.getClosingPrice(),
            indexData.getClosingPrice()
        );
    }

    public static IndexDataWithInfoDto fixCurrentPriceDto(IndexDataWithInfoDto dto, double fixedCurrentPrice) {
        return new IndexDataWithInfoDto(dto.indexInfoId(),
            dto.indexClassification(),
            dto.indexName(),
            dto.versus(),
            dto.fluctuationRate(),
            dto.currentPrice(),
            new BigDecimal(fixedCurrentPrice));
    }

}
