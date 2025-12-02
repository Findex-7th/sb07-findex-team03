package com.team3.findex.common.openapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.findex.common.openapi.IndexApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexApiService {
    private final IndexOpenApiClient indexOpenApiClient;
    private final ObjectMapper objectMapper;

    public IndexApiResponse getByDate(LocalDate date) {
        try {
            JsonNode itemsNode = objectMapper.readTree(indexOpenApiClient.getByDate(date))
                    .path("response")
                    .path("body")
                    .path("items");

            objectMapper.convertValue(objectMapper.convertValue(itemsNode, new TypeReference<List<IndexApiResponse>>() {}));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
