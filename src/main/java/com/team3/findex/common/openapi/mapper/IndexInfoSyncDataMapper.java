package com.team3.findex.common.openapi.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.team3.findex.common.openapi.dto.IndexApiResponse;
import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import com.team3.findex.common.openapi.service.IndexApiService;
import lombok.RequiredArgsConstructor;

/**
 * API 데이터 제공 서비스 클래스
 * <p>
 * OpenApiService가 API로부터 IndexApiResponse로 받은 응답을
 * 도메인 서비스에서 사용하기 편한 형태로 변환
 * </p>
 */
@RequiredArgsConstructor
class IndexInfoSyncDataMapper {

    private final IndexApiService indexApiService;

    /**
     * @param indexApiResponse 변환할 IndexApiResponse
     * @return 변환 엔터티
     */
    protected static IndexInfoSyncData toIndexInfoSyncData(IndexApiResponse indexApiResponse) {
        return new IndexInfoSyncData(
                indexApiResponse.getIndexCategory(),
                indexApiResponse.getIndexName(),
                indexApiResponse.getItemsCount(),
                LocalDate.parse(indexApiResponse.getBasePoint(), DateTimeFormatter.ofPattern("yyyyMMdd")),
                Double.parseDouble(indexApiResponse.getBaseIndex()));
    }

    /**
     * @param indexApiResponses 변환할 IndexApiResponse 리스트
     * @return 변환 엔터티 리스트
     */
    protected static List<IndexInfoSyncData> toIndexInfoSyncData(List<IndexApiResponse> indexApiResponses) {
        return indexApiResponses.stream()
                .map(data ->
                        new IndexInfoSyncData(
                                data.getIndexCategory(),
                                data.getIndexName(),
                                data.getItemsCount(),
                                LocalDate.parse(data.getBasePoint(), DateTimeFormatter.ofPattern("yyyyMMdd")),
                                Double.parseDouble(data.getBaseIndex())
                        )
                ).toList();
    }


}

