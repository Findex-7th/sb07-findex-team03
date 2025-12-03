package com.team3.findex.common.openapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.findex.common.openapi.dto.IndexApiResponse;
import com.team3.findex.common.util.HolidayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 주가 OpenApi를 불러와서 IndexApiResponse에 담는다
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
     * 특정 날짜의 모든 주가 지수를 가져옵니다.
     * @param date 타겟 날짜
     * @return 그 날짜의 모든 주가 지수 List
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
     * 특정 이름의 모든 주가 지수를 가져옵니다.
     * @param indexName 주가 이름
     * @return 그 주가 이름의 모든 주가 지수 정보
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
     * 어떤 주가의 특정 날짜 정보를 가져옵니다.
     * @param indexName 주가 이름
     * @param date 가져올 날짜
     * @return Optional<주가정보>
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
     * 오늘의 주가 정보를 가져옵니다 (작동 안 할 듯)
     * @param indexName 주식 이름
     * @return 해당 응답
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
     * 1 영업일 전의 주가 정보를 가져옵니다
     *
     * @param indexName 주식 이름
     * @return 해당 응답
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

    private LocalDate getLastWeekday(LocalDate date) {
        do {
            date = date.minusDays(1);
        } while (HolidayUtil.isHoliday(date));

        return date;
    }
}


