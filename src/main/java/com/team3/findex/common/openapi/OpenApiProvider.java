package com.team3.findex.common.openapi;

import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import com.team3.findex.common.openapi.dto.IndexSyncData;
import com.team3.findex.common.openapi.mapper.OpenApiMapper;
import com.team3.findex.common.openapi.service.IndexApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OpenApiProvider {
    private final IndexApiService indexApiService;
    private final OpenApiMapper openApiMapper;

    /**
     * 오늘의 모든 주가 지수를 가져옵니다.
     * @param date 타겟 날짜
     * @return 그 날짜의 모든 주가 지수 List
     */
    public List<IndexInfoSyncData> getIndexInfoDataByDate(LocalDate date) {
        return indexApiService.getByDate(date).stream()
                .map(openApiMapper::toIndexInfoSyncData)
                .toList();
    }

    /**
     * 오늘의 모든 주가 지수를 가져옵니다.
     * @param date 타겟 날짜
     * @return 그 날짜의 모든 주가 지수 List
     */
    public List<IndexSyncData> getIndexDataByDate(LocalDate date) {
        return indexApiService.getByDate(date).stream()
                .map(openApiMapper::toIndexSyncData)
                .toList();
    }

    /**
     * 특정 이름의 모든 주가 지수를 가져옵니다.
     * @param indexName 주가 이름
     * @return 그 주가 이름의 모든 주가 지수 정보
     */
    public List<IndexInfoSyncData> getIndexInfoByName(String indexName) {
        return indexApiService.getByName(indexName).stream()
                .map(openApiMapper::toIndexInfoSyncData)
                .toList();
    }

    /**
     * 특정 이름의 모든 주가 지수를 가져옵니다.
     * @param indexName 주가 이름
     * @return 그 주가 이름의 모든 주가 지수 정보
     */
    public List<IndexSyncData> getIndexByName(String indexName) {
        return indexApiService.getByName(indexName).stream()
                .map(openApiMapper::toIndexSyncData)
                .toList();
    }

    /**
     * 어떤 주가의 특정 날짜 정보를 가져옵니다.
     * @param indexName 주가 이름
     * @param date 가져올 날짜
     * @return Optional<주가정보>
     */
    public Optional<IndexInfoSyncData> getIndexInfoByNameAndDate(String indexName, LocalDate date) {
        return indexApiService.getByNameAndDate(indexName, date).map(openApiMapper::toIndexInfoSyncData);
    }

    public Optional<IndexSyncData> getIndexByNameAndDate(String indexName, LocalDate date) {
        return indexApiService.getByNameAndDate(indexName, date).map(openApiMapper::toIndexSyncData);
    }

    /**
     * 오늘의 주가 정보를 가져옵니다 (작동 안 할 듯)
     * @param indexName 주식 이름
     * @return 해당 응답
     */
    public Optional<IndexInfoSyncData> getIndexInfoByNameOfToday(String indexName) {
        return indexApiService.getByNameOfToday(indexName).map(openApiMapper::toIndexInfoSyncData);
    }

    public Optional<IndexSyncData> getIndexByNameOfToday(String indexName) {
        return indexApiService.getByNameOfToday(indexName).map(openApiMapper::toIndexSyncData);
    }

    /**
     * 1 영업일 전의 주가 정보를 가져옵니다
     *
     * @param indexName 주식 이름
     * @return 해당 응답
     */
    public Optional<IndexInfoSyncData> getIndexInfoByNameYesterday(String indexName) {
        return indexApiService.getByNameYesterday(indexName).map(openApiMapper::toIndexInfoSyncData);
    }

    public Optional<IndexSyncData> getIndexByNameYesterday(String indexName) {
        return indexApiService.getByNameYesterday(indexName).map(openApiMapper::toIndexSyncData);
    }

    public List<IndexInfoSyncData> getIndexInfoByDayRange(LocalDate start, LocalDate end) {
        return indexApiService.getByDayRange(start, end)
                .stream()
                .map(openApiMapper::toIndexInfoSyncData)
                .toList();
    }

    public List<IndexSyncData> getIndexByDayRange(LocalDate start, LocalDate end) {
        return indexApiService.getByDayRange(start, end)
                .stream()
                .map(openApiMapper::toIndexSyncData)
                .toList();
    }
}
