package com.team3.findex.common.openapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.findex.common.openapi.dto.IndexApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
class IndexOpenApiClientTest {

    @Autowired
    private IndexOpenApiClient openApiService;

    @Test
    @DisplayName("api 호출 테스트")
    void 호출_테스트() throws JsonProcessingException {
        // given
        String openApiDataByDate = openApiService.getByDate(LocalDate.of(2025, 11, 28));

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

}