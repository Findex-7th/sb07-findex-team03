package com.team3.findex.common.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.team3.findex.common.openapi.dto.IndexApiResponse;
import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import com.team3.findex.common.openapi.dto.IndexSyncData;
import com.team3.findex.common.openapi.mapper.OpenApiMapper;
import com.team3.findex.common.openapi.service.IndexApiService;
import com.team3.findex.common.util.HolidayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * OpenAPI 데이터를 제공하는 Provider 클래스
 * <p>
 * IndexApiService로부터 데이터를 조회하고, 필요한 DTO 형식으로 변환하여 제공합니다.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class OpenApiProvider {
    private final IndexApiService indexApiService;
    private final OpenApiMapper openApiMapper;

    /**
     * 특정 날짜의 모든 지수 정보를 조회합니다.
     *
     * @param date 조회할 날짜
     * @return 해당 날짜의 모든 지수 정보(IndexInfo용) 목록
     */
    public List<IndexInfoSyncData> getIndexInfoDataByDate(LocalDate date) {
        return indexApiService.getByDate(date).stream()
                .map(openApiMapper::toIndexInfoSyncData)
                .toList();
    }

    /**
     * 특정 날짜의 모든 지수 데이터를 조회합니다.
     *
     * @param date 조회할 날짜
     * @return 해당 날짜의 모든 지수 데이터(IndexData용) 목록
     */
    public List<IndexSyncData> getIndexDataByDate(LocalDate date) {
        return indexApiService.getByDate(date).stream()
                .map(openApiMapper::toIndexSyncData)
                .toList();
    }

    /**
     * 특정 지수 이름의 모든 정보를 조회합니다.
     *
     * @param indexName 조회할 지수 이름
     * @return 해당 지수 이름의 모든 정보(IndexInfo용) 목록
     */
    public List<IndexInfoSyncData> getIndexInfoByName(String indexName) {
        return indexApiService.getByName(indexName).stream()
                .map(openApiMapper::toIndexInfoSyncData)
                .toList();
    }

    /**
     * 특정 지수 이름의 모든 데이터를 조회합니다.
     *
     * @param indexName 조회할 지수 이름
     * @return 해당 지수 이름의 모든 데이터(IndexData용) 목록
     */
    public List<IndexSyncData> getIndexByName(String indexName) {
        return indexApiService.getByName(indexName).stream()
                .map(openApiMapper::toIndexSyncData)
                .toList();
    }

    /**
     * 특정 지수의 특정 날짜 정보를 조회합니다.
     *
     * @param indexName 조회할 지수 이름
     * @param date 조회할 날짜
     * @return 지수 정보(IndexInfo용)를 담은 Optional (데이터가 없으면 empty)
     */
    public Optional<IndexInfoSyncData> getIndexInfoByNameAndDate(String indexName, LocalDate date) {
        return indexApiService.getByNameAndDate(indexName, date).map(openApiMapper::toIndexInfoSyncData);
    }

    /**
     * 특정 지수의 특정 날짜 데이터를 조회합니다.
     *
     * @param indexName 조회할 지수 이름
     * @param date 조회할 날짜
     * @return 지수 데이터(IndexData용)를 담은 Optional (데이터가 없으면 empty)
     */
    public Optional<IndexSyncData> getIndexByNameAndDate(String indexName, LocalDate date) {
        return indexApiService.getByNameAndDate(indexName, date).map(openApiMapper::toIndexSyncData);
    }

    /**
     * 오늘 날짜의 지수 정보를 조회합니다.
     * <p>
     * 참고: 실시간 데이터가 아닐 수 있으며, 장 마감 후에만 데이터가 제공될 수 있습니다.
     * </p>
     *
     * @param indexName 조회할 지수 이름
     * @return 지수 정보(IndexInfo용)를 담은 Optional (데이터가 없으면 empty)
     */
    public Optional<IndexInfoSyncData> getIndexInfoByNameOfToday(String indexName) {
        return indexApiService.getByNameOfToday(indexName).map(openApiMapper::toIndexInfoSyncData);
    }

    /**
     * 오늘 날짜의 지수 데이터를 조회합니다.
     * <p>
     * 참고: 실시간 데이터가 아닐 수 있으며, 장 마감 후에만 데이터가 제공될 수 있습니다.
     * </p>
     *
     * @param indexName 조회할 지수 이름
     * @return 지수 데이터(IndexData용)를 담은 Optional (데이터가 없으면 empty)
     */
    public Optional<IndexSyncData> getIndexByNameOfToday(String indexName) {
        return indexApiService.getByNameOfToday(indexName).map(openApiMapper::toIndexSyncData);
    }

    /**
     * 1 영업일 전의 지수 정보를 조회합니다.
     * <p>
     * 주말과 공휴일을 제외한 가장 최근 영업일의 데이터를 반환합니다.
     * </p>
     *
     * @param indexName 조회할 지수 이름
     * @return 지수 정보(IndexInfo용)를 담은 Optional (데이터가 없으면 empty)
     */
    public Optional<IndexInfoSyncData> getIndexInfoByNameYesterday(String indexName) {
        return indexApiService.getByNameYesterday(indexName).map(openApiMapper::toIndexInfoSyncData);
    }

    /**
     * 1 영업일 전의 지수 데이터를 조회합니다.
     * <p>
     * 주말과 공휴일을 제외한 가장 최근 영업일의 데이터를 반환합니다.
     * </p>
     *
     * @param indexName 조회할 지수 이름
     * @return 지수 데이터(IndexData용)를 담은 Optional (데이터가 없으면 empty)
     */
    public Optional<IndexSyncData> getIndexByNameYesterday(String indexName) {
        return indexApiService.getByNameYesterday(indexName).map(openApiMapper::toIndexSyncData);
    }

    /**
     * 특정 날짜 범위의 지수 정보를 조회합니다.
     *
     * @param start 시작 날짜 (포함)
     * @param end 종료 날짜 (포함)
     * @return 해당 기간의 모든 지수 정보(IndexInfo용) 목록
     */
    public List<IndexInfoSyncData> getIndexInfoByDayRange(LocalDate start, LocalDate end) {
        return indexApiService.getByDayRange(start, end)
                .stream()
                .map(openApiMapper::toIndexInfoSyncData)
                .toList();
    }

    /**
     * 특정 날짜 범위의 지수 데이터를 조회합니다.
     *
     * @param start 시작 날짜 (포함)
     * @param end 종료 날짜 (포함)
     * @return 해당 기간의 모든 지수 데이터(IndexData용) 목록
     */
    public List<IndexSyncData> getIndexByDayRange(LocalDate start, LocalDate end) {
        return indexApiService.getByDayRange(start, end)
                .stream()
                .map(openApiMapper::toIndexSyncData)
                .toList();
    }
}
