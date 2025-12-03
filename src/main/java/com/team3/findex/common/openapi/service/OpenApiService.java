package com.team3.findex.common.openapi.service;

import com.team3.findex.common.openapi.IndexApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OpenApiService {

    @Value("${open-api.key}")
    private String serviceKey;

    @Value("${open-api.url}")
    private String url;

    private final RestTemplate restTemplate;

    public String getOpenApiDataByDate(LocalDate date) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("basDt", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), String.class);
    }

    public IndexApiResponse getOpenApiDataByDate2(LocalDate date) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json")
                .queryParam("serviceKey", serviceKey)
                .queryParam("basDt", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return restTemplate.getForObject(uri.toUriString(), IndexApiResponse.class);
    }

}
