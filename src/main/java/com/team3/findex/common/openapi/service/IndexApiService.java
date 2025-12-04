package com.team3.findex.common.openapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.findex.common.openapi.dto.IndexApiResponse;
import com.team3.findex.common.util.HolidayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 지수 OpenAPI 데이터를 조회하고 가공하는 서비스
 * <p>
 * 외부 OpenAPI로부터 지수 시세 정보를 조회하여 IndexApiResponse 객체로 변환합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class IndexApiService {
    private final IndexOpenApiClient indexOpenApiClient;
    private final ObjectMapper objectMapper;

    /*
     TODO 만약 주가 정보가 없는 날의 주가 정보를 요청했을시에
     에러를 줄 것인지? 빈 List나 Optional을 줄 것인지?
     */

    /**
     * 특정 날짜의 모든 지수 데이터를 조회합니다.
     *
     * @param date 조회할 날짜
     * @return 해당 날짜의 모든 지수 데이터 목록
     * @throws RuntimeException JSON 파싱 중 오류 발생 시
     */
    public List<IndexApiResponse> getByDate(LocalDate date) {
        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByDate(date))
                    .path("response")
                    .path("body")
                    .path("items");

            return objectMapper.convertValue(itemsNode, new TypeReference<List<IndexApiResponse>>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 특정 지수 이름의 모든 데이터를 조회합니다.
     *
     * @param indexName 조회할 지수 이름
     * @return 해당 지수 이름의 모든 데이터 목록
     * @throws RuntimeException JSON 파싱 중 오류 발생 시
     */
    public List<IndexApiResponse> getByName(String indexName) {
        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByName(indexName))
                    .path("response")
                    .path("body")
                    .path("items");

            return objectMapper.convertValue(itemsNode, new TypeReference<List<IndexApiResponse>>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 특정 지수의 특정 날짜 데이터를 조회합니다.
     *
     * @param indexName 조회할 지수 이름
     * @param date 조회할 날짜
     * @return 지수 데이터를 담은 Optional (데이터가 없으면 empty)
     * @throws RuntimeException JSON 파싱 중 오류 발생 시
     */
    public Optional<IndexApiResponse> getByNameAndDate(String indexName, LocalDate date) {
        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByNameAndDate(indexName, date))
                    .path("response")
                    .path("body")
                    .path("items");

            return Optional.ofNullable((IndexApiResponse) objectMapper.convertValue(itemsNode.get("item"), new TypeReference<List<IndexApiResponse>>() {
            }));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 오늘 날짜의 지수 데이터를 조회합니다.
     * <p>
     * 참고: 실시간 데이터가 아닐 수 있으며, 장 마감 후에만 데이터가 제공될 수 있습니다.
     * </p>
     *
     * @param indexName 조회할 지수 이름
     * @return 지수 데이터를 담은 Optional (데이터가 없으면 empty)
     * @throws RuntimeException JSON 파싱 중 오류 발생 시
     */
    public Optional<IndexApiResponse> getByNameOfToday(String indexName) {
        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByNameAndDate(indexName, LocalDate.now()))
                    .path("response")
                    .path("body")
                    .path("items");

            return Optional.ofNullable(objectMapper.convertValue(itemsNode, new TypeReference<IndexApiResponse>() {
            }));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 1 영업일 전의 지수 데이터를 조회합니다.
     * <p>
     * 주말과 공휴일을 제외한 가장 최근 영업일의 데이터를 반환합니다.
     * </p>
     *
     * @param indexName 조회할 지수 이름
     * @return 지수 데이터를 담은 Optional (데이터가 없으면 empty)
     * @throws RuntimeException JSON 파싱 중 오류 발생 시
     */
    public Optional<IndexApiResponse> getByNameYesterday(String indexName) {
        LocalDate date = getLastWeekday(LocalDate.now());

        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByNameAndDate(indexName, date))
                    .path("response")
                    .path("body")
                    .path("items");

            return Optional.ofNullable(
                    objectMapper.convertValue(itemsNode, new TypeReference<IndexApiResponse>() {})
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 주어진 날짜로부터 가장 최근 영업일을 계산합니다.
     *
     * @param date 기준 날짜
     * @return 가장 최근 영업일 (주말 및 공휴일 제외)
     */
    private LocalDate getLastWeekday(LocalDate date) {
        do {
            date = date.minusDays(1);
        } while (HolidayUtil.isHoliday(date));

        return date;
    }

    /**
     * 특정 날짜 범위의 지수 데이터를 조회합니다.
     *
     * @param start 시작 날짜 (포함)
     * @param end 종료 날짜 (포함)
     * @return 해당 기간의 모든 지수 데이터 목록
     * @throws RuntimeException JSON 파싱 중 오류 발생 시
     */
    public List<IndexApiResponse> getByDayRange(LocalDate start, LocalDate end) {
        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByDateRange(start, end))
                    .path("response")
                    .path("body")
                    .path("items");

            return objectMapper.convertValue(itemsNode, new TypeReference<List<IndexApiResponse>>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


