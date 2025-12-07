package com.team3.findex.domain.syncjob.openapitester;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.openapitester.dto.ApiResponseDto;
import com.team3.findex.domain.syncjob.openapitester.mapper.OpenAPIMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenApiTester {

    @Value("${open-api.key}")
    protected String serviceKey;

    @Value("${open-api.url}")
    protected String url;

    private final RestClient restClient;

    private final OpenAPIMapper openAPIMapper;

    public List<IndexInfo> fetchAllApiToIndexInfo() {
        ApiResponseDto dto = restClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1160100/service/GetMarketIndexInfoService/getStockMarketIndex")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resultType", "json")
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 163)
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
        dto.getResponse().getBody().getItems().getItemList().forEach(item -> log.info("item: {}", item));
        return dto.getResponse().getBody().getItems().getItemList().stream().map(openAPIMapper::toIndexInfoEntity)
                .toList();
    }

    public List<IndexData> fetchApiByParamsToIndexData(
            String idxNm,
            String beginBasDt,
            String endBasDt,
            IndexInfo indexInfo){
        ApiResponseDto dto = restClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1160100/service/GetMarketIndexInfoService/getStockMarketIndex")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resultType", "json")
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 500)
                            .queryParam("idxNm", idxNm)
                            .queryParam("basDt", beginBasDt)
                            .queryParam("endBasDt", endBasDt)
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
        dto.getResponse().getBody().getItems().getItemList().forEach(item -> log.info("item: {}", item));
        return dto.getResponse().getBody().getItems().getItemList().stream().map(item -> openAPIMapper.toIndexDataEntity(item, indexInfo)).toList();
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
