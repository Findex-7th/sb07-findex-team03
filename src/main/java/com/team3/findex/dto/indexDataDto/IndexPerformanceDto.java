package com.team3.findex.dto.indexDataDto;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexDataUser;
import com.team3.findex.domain.index.IndexInfo;

public record IndexPerformanceDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    Double versus,
    Double fluctuationRate,
    Double currentPrice,
    Double beforePrice
) {
    public IndexPerformanceDto(IndexInfo indexInfo, IndexData indexData) {
        this.indexInfoId = indexInfo.getId();
        this.indexClassification = indexInfo.getIndexClassification();
        this.indexName = indexInfo.getIndexName();
        this.versus = indexData.getVersus();
        this.fluctuationRate = indexData.getFluctuationRate();
        this.currentPrice = indexData.getMarketPrice(); //?? 시가??
        this.beforePrice = indexData.getClosingPrice(); //?? 종가??
    }
}
