package com.team3.findex.common.openapi.mapper;

import com.team3.findex.common.openapi.IndexApiResponse;
import com.team3.findex.common.openapi.dto.IndexSyncData;
import com.team3.findex.common.openapi.service.IndexApiService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * API 데이터 제공 서비스 클래스
 * <p>
 * OpenApiService가 API로부터 IndexApiResponse로 받은 응답을
 * 도메인 서비스에서 사용하기 편한 형태로 변환
 * </p>
 */
@RequiredArgsConstructor
public class IndexSyncMapper {

    private final IndexApiService indexApiService;

    /**
     * @param indexApiResponse 변환할 IndexApiResponse
     * @return 변환 엔터티
     */
    public static IndexSyncData toIndexSyncData(IndexApiResponse indexApiResponse) {
        return new IndexSyncData(
                indexApiResponse.getBaseDate(),
                indexApiResponse.getMarketPrice(),
                indexApiResponse.getClosingPrice(),
                indexApiResponse.getHighPrice(),
                indexApiResponse.getLowPrice(),
                indexApiResponse.getTradeVolume(),
                indexApiResponse.getVariationSign(),
                indexApiResponse.getChangeRate(),
                indexApiResponse.getTradePrice(),
                indexApiResponse.getMarketTotalAmount()
        );
    }

    /**
     * @param indexApiResponses 변환할 IndexApiResponse 리스트
     * @return 변환 엔터티 리스트
     */
    public static List<IndexSyncData> toIndexSyncData(List<IndexApiResponse> indexApiResponses) {
        return indexApiResponses.stream()
                .map(indexApiResponse -> new IndexSyncData(
                        indexApiResponse.getBaseDate(),
                        indexApiResponse.getMarketPrice(),
                        indexApiResponse.getClosingPrice(),
                        indexApiResponse.getHighPrice(),
                        indexApiResponse.getLowPrice(),
                        indexApiResponse.getTradeVolume(),
                        indexApiResponse.getVariationSign(),
                        indexApiResponse.getChangeRate(),
                        indexApiResponse.getTradePrice(),
                        indexApiResponse.getMarketTotalAmount()
                )).toList();
    }


}

