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

    protected String getByDate(LocalDate date) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("basDt", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

    protected String getByName(String indexName) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("idxNm", indexName)
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

    protected String getByNameAndDate(String indexName, LocalDate date) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("idxNm", indexName)
                .queryParam("basDt", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

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
