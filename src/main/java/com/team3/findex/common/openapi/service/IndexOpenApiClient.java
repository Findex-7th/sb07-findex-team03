package com.team3.findex.common.openapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class IndexOpenApiClient {

    @Value("${open-api.key}")
    protected String serviceKey;

    @Value("${open-api.url}")
    protected String url;

    private final RestTemplate restTemplate;

    /**
     * 특정 날짜의 데이터를 외부 OpenAPI 서비스에서 조회합니다.
     *
     * @param date 데이터를 조회할 대상 날짜
     * @return 지정한 날짜의 JSON 형식 문자열 데이터
     */
    protected String getByDate(LocalDate date) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("basDt", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

    /**
     * 지정한 지수 이름의 데이터를 외부 OpenAPI 서비스에서 조회합니다.
     *
     * @param indexName 데이터를 조회할 지수 이름
     * @return 지정한 지수 이름의 정보를 포함한 JSON 형식 문자열 데이터
     */
    protected String getByName(String indexName) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("idxNm", indexName)
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

    /**
     * 지정한 지수 이름과 날짜의 데이터를 외부 OpenAPI 서비스에서 조회합니다.
     *
     * @param indexName 데이터를 조회할 지수 이름
     * @param date 데이터를 조회할 대상 날짜
     * @return 지정한 지수 이름과 날짜의 정보를 포함한 JSON 형식 문자열 데이터
     */
    protected String getByNameAndDate(String indexName, LocalDate date) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("idxNm", indexName)
                .queryParam("basDt", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

    /**
     * 지정한 날짜 범위의 데이터를 조회합니다.
     *
     * @param start 범위의 시작 날짜(포함)
     * @param end   범위의 종료 날짜(포함)
     * @return 지정한 날짜 범위의 데이터를 포함한 JSON 형식의 문자열
     */
    protected String getByDateRange(LocalDate start, LocalDate end) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("beginBasDt", start.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .queryParam("endBasDt", end.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }


}
