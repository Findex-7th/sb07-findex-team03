package com.team3.findex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.findex.domain.openapi.IndexApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
class OpenApiServiceTest {

    @Autowired
    private OpenApiService openApiService;

    @Test
    @DisplayName("api 호출 테스트")
    void 호출_테스트() throws JsonProcessingException {
        // given
        String openApiDataByDate = openApiService.getOpenApiDataByDate(LocalDate.of(2025, 11, 28));

        // when
        System.out.println("openApiDataByDate = " + openApiDataByDate);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(openApiDataByDate);
        JsonNode items = jsonNode.get("response").get("body").get("items").get("item");
        JsonNode items2 = jsonNode.path("response").path("body").path("items").path("item");

        List<IndexApiResponse> indexApiResponses = objectMapper.convertValue(items2, new TypeReference<List<IndexApiResponse>>() {
        });

        for (IndexApiResponse indexApiRespons : indexApiResponses) {
            System.out.println("indexApiRespons = " + indexApiRespons.getIndexName());
        }


    }


    @Test
    @DisplayName("api 호출 클래스 매핑 테스트")
    void 매핑_호출_테스트() throws JsonProcessingException {
        // given
        IndexApiResponse result = openApiService.getOpenApiDataByDate2(LocalDate.of(2025, 11, 28));

        System.out.println("result = " + result);

        System.out.println("result.getIndexName() = " + result.getIndexName());


    }
}