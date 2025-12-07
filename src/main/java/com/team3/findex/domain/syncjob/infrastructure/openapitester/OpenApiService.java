package com.team3.findex.domain.syncjob.infrastructure.openapitester;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.infrastructure.openapitester.dto.ApiResponseDto;
import com.team3.findex.domain.syncjob.infrastructure.openapitester.mapper.OpenAPIMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenApiService {

    @Value("${open-api.key}")
    protected String serviceKey;

    private final RestClient restClient;

    private final OpenAPIMapper openAPIMapper;

    public List<IndexInfo> fetchAllApiToIndexInfo() {

        LocalDate now = LocalDate.now();
        String endBasDt = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        String beginBasDt = now.minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);

        log.info("지수 목록 갱신을 위한 최근 데이터 조회 기간: {} ~ {}", beginBasDt, endBasDt);

        List<IndexInfo> allResult = new ArrayList<>();
        int pageNo = 1;
        int numOfRows = 1000;

        while (true) {
            ApiResponseDto dto = callIndexDataApi(null, beginBasDt, endBasDt, pageNo, numOfRows);
            if (!isValidResponse(dto)) break;

            ApiResponseDto.Body body = dto.getResponse().getBody();

            if (body.getItems() != null && body.getItems().getItemList() != null) {
                List<IndexInfo> pageInfos = body.getItems().getItemList().stream()
                        .map(openAPIMapper::toIndexInfoEntity)
                        .toList();
                allResult.addAll(pageInfos);
            }

            int totalCount = body.getTotalCount();
            if (pageNo * numOfRows >= totalCount || totalCount == 0) break;

            pageNo++;
        }
        List<IndexInfo> distinctInfos = allResult.stream()
                .filter(distinctByKey(info -> info.getIndexClassification() + "|" + info.getIndexName()))
                .toList();

        log.info("지수 목록 추출 완료: 원본 {}건 -> 중복 제거 후 {}건", allResult.size(), distinctInfos.size());
        return distinctInfos;
    }

    public List<IndexData> fetchApiByParamsToIndexData(
            String idxNm,
            String beginBasDt,
            String endBasDt,
            IndexInfo indexInfo) {

        List<IndexData> allResult = new ArrayList<>();
        int pageNo = 1;
        int numOfRows = 1000;

        while (true) {
            ApiResponseDto dto = callIndexDataApi(idxNm, beginBasDt, endBasDt, pageNo, numOfRows);

            if (!isValidResponse(dto)) {
                break;
            }

            ApiResponseDto.Body body = dto.getResponse().getBody();

            if (body.getItems() != null && body.getItems().getItemList() != null) {
                List<IndexData> pageData = body.getItems().getItemList().stream()
                        .filter(item -> isSameIndex(item, indexInfo))
                        .map(item -> openAPIMapper.toIndexDataEntity(item, indexInfo))
                        .toList();
                allResult.addAll(pageData);
            }

            int totalCount = body.getTotalCount();
            if (pageNo * numOfRows >= totalCount || totalCount == 0) {
                break;
            }

            pageNo++;
        }

        return allResult;
    }

    private ApiResponseDto callIndexDataApi(String idxNm, String beginBasDt, String endBasDt, int pageNo, int numOfRows){
        return restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1160100/service/GetMarketIndexInfoService/getStockMarketIndex")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resultType", "json")
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", numOfRows)
                            .queryParam("beginBasDt", beginBasDt)
                            .queryParam("endBasDt", endBasDt);

                    if (idxNm != null && !idxNm.isBlank()) {
                        builder.queryParam("idxNm", idxNm);
                    }

                    return builder.build();
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

    private boolean isValidResponse(ApiResponseDto dto) {
        return dto != null
                && dto.getResponse() != null
                && dto.getResponse().getBody() != null;
    }

    private boolean isSameIndex(ApiResponseDto.ApiItemDto item, IndexInfo indexInfo) {
        boolean nameMatch = item.getIdxNm().equals(indexInfo.getIndexName());
        boolean classificationMatch = item.getIdxCsf().equals(indexInfo.getIndexClassification());

        return nameMatch && classificationMatch;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
