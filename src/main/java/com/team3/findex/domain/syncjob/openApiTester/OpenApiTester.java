package com.team3.findex.domain.syncjob.openApiTester;

import com.team3.findex.domain.syncjob.openApiTester.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenApiTester {

    @Value("${open-api.key}")
    protected String serviceKey;

    @Value("${open-api.url}")
    protected String url;

    private final RestClient restClient;

    public ApiResponseDto fetchApiByOptions(String indexName, String baseDate, int pageNo, int numOfRows) {
        return restClient.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1160100/service/GetMarketIndexInfoService/getStockMarketIndex")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resultType", "json")
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", numOfRows)
                            .build();


                    return addOptionalParams(uriBuilder, indexName, baseDate);
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("클라이언트 에러: " + response.getStatusCode() + " " + response.getStatusText());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("서버 에러: " + response.getStatusCode());
                })
                .body(ApiResponseDto.class);
    }

    public ApiResponseDto fetchAllApi() {
        return restClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1160100/service/GetMarketIndexInfoService/getStockMarketIndex")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resultType", "json")
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 200)
                            .build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("클라이언트 에러: " + response.getStatusCode() + " " + response.getStatusText());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("서버 에러: " + response.getStatusCode());
                })
                .body(ApiResponseDto.class);
    }


    private URI addOptionalParams(UriBuilder builder, String indexName, String baseDate) {
        if (indexName != null && !indexName.isBlank()) {
            builder.queryParam("idxNm", indexName);
        }
        if (baseDate != null && !baseDate.isBlank()) {
            builder.queryParam("basDt", baseDate);
        }
        return builder.build();
    }

}
