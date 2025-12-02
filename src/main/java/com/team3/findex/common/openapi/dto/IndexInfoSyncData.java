package com.team3.findex.common.openapi.dto;

import java.time.LocalDate;


/**
 * 지수 정보 연동 데이터(openApi)
 * <p>
 * 가져온 지수 정보만 담은 DTO
 * 지수 정보 등록 및 수정에 사용
 * </p>
 * @param indexClassification 지수 분류명
 * @param indexName 지수명
 * @param employedItemsCount 채용 종목수
 * @param basePointInTime 기준 시점
 * @param baseIndex 기준 지수
 */
public record IndexInfoSyncData(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    Double baseIndex
) {}
