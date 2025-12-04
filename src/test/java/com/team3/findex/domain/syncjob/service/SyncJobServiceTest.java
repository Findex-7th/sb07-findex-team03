package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.openApiTester.OpenApiTester;
import com.team3.findex.domain.syncjob.openApiTester.dto.ApiResponseDto;
import com.team3.findex.repository.IndexInfoRepository;
import com.team3.findex.domain.syncjob.repository.SyncJobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class SyncJobServiceIntegrationTest {

    @Autowired
    private SyncJobService syncJobService;

    @Autowired
    private IndexInfoRepository indexInfoRepository;

    @Autowired
    private SyncJobRepository syncJobRepository;

    @Autowired
    private OpenApiTester openApiTester;

    @Test
    @DisplayName("API 데이터를 가져와서 DB에 IndexInfo와 SyncJob이 정상적으로 저장되는지 확인")
    void syncIndexInfos_IntegrationTest() {
        // given
        String worker = "IntegrationTester";
        String testIndexName = "테스트_코스피";

        ApiResponseDto mockResponse = createMockResponse(testIndexName);

        given(openApiTester.fetchAllApi()).willReturn(mockResponse);


        List<SyncJobDto> results = syncJobService.syncIndexInfos(worker);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).result()).isEqualTo(Result.SUCCESS);

        List<IndexInfo> savedIndexInfos = indexInfoRepository.findAll();
        assertThat(savedIndexInfos).isNotEmpty();

        IndexInfo savedInfo = savedIndexInfos.stream()
                .filter(info -> info.getIndexName().equals(testIndexName))
                .findFirst()
                .orElseThrow();

        assertThat(savedInfo.getIndexClassification()).isEqualTo("KOSPI_TEST");
        assertThat(savedInfo.getEmployedItemsCount()).isEqualTo(100);

        List<SyncJob> savedJobs = syncJobRepository.findAll();
        assertThat(savedJobs).isNotEmpty();

        SyncJob lastJob = savedJobs.get(savedJobs.size() - 1); // 가장 최근 로그
        assertThat(lastJob.getWorker()).isEqualTo(worker);
        assertThat(lastJob.getResult()).isEqualTo(Result.SUCCESS);
        assertThat(lastJob.getIndexInfo().getId()).isEqualTo(savedInfo.getId()); // 연관관계 확인
    }

    private ApiResponseDto createMockResponse(String indexName) {
        ApiResponseDto.ApiItemDto itemDto = new ApiResponseDto.ApiItemDto();
        itemDto.setIdxNm(indexName);
        itemDto.setIdxCsf("KOSPI_TEST");
        itemDto.setEpyItmsCnt(100);
        itemDto.setBasPntm("20230101");
        itemDto.setBasIdx(100.0);

        ApiResponseDto.Items items = new ApiResponseDto.Items();
        items.setItemList(List.of(itemDto));

        ApiResponseDto.Body body = new ApiResponseDto.Body();
        body.setItems(items);

        ApiResponseDto.Response response = new ApiResponseDto.Response();
        response.setBody(body);

        ApiResponseDto root = new ApiResponseDto();
        root.setResponse(response);

        return root;
    }
}