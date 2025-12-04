package com.team3.findex.domain.index.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.team3.findex.common.openapi.OpenApiProvider;
import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import com.team3.findex.domain.autosync.service.AutoSyncService;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.repository.IndexInfoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class IndexInfoServiceImplTest {

  @Mock
  private IndexInfoRepository indexInfoRepository;

  @Mock
  private OpenApiProvider openApiProvider;

  @Mock
  private AutoSyncService autoSyncService;

  @InjectMocks
  private IndexInfoServiceImpl indexInfoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // 모든 @Mock 주입
  }

  @Test
  void autoSync_신규_지수_자동_등록_성공() {
    // given
    IndexInfoSyncData sync = new IndexInfoSyncData(
        "국내주식",
        "코스닥150",
        150,
        LocalDate.of(2024, 12, 1),
        1000.0
    );

    when(openApiProvider.getIndexInfoDataByDate(any()))
        .thenReturn(List.of(sync));

    when(indexInfoRepository.findByIndexClassificationAndIndexName("국내주식", "코스닥150"))
        .thenReturn(Optional.empty());

    // when
    indexInfoService.autoSyncFromOpenApi();

    // then
    verify(indexInfoRepository, times(1)).save(any(IndexInfo.class));
    verify(autoSyncService, times(1)).create(any(IndexInfo.class));
  }

  @Test
  void autoSync_기존_지수_자동_수정_성공() {
    // given
    IndexInfoSyncData sync = new IndexInfoSyncData(
        "국내주식",
        "코스피200",
        200,
        LocalDate.of(2024, 12, 2),
        1200.0
    );

    when(openApiProvider.getIndexInfoDataByDate(any()))
        .thenReturn(List.of(sync));

    IndexInfo existing = new IndexInfo(
        "국내주식",
        "코스피200",
        150,
        LocalDate.of(2023, 12, 1),
        900.0,
        false,
        SourceType.OPEN_API
    );

    when(indexInfoRepository.findByIndexClassificationAndIndexName("국내주식", "코스피200"))
        .thenReturn(Optional.of(existing));

    // when
    indexInfoService.autoSyncFromOpenApi();

    // then
    verify(indexInfoRepository, times(1)).save(existing);
    verify(autoSyncService, never()).create(any(IndexInfo.class));

    // 필드 수정 검증
    assertThat(existing.getEmployedItemsCount()).isEqualTo(200);
    assertThat(existing.getBaseIndex()).isEqualTo(1200.0);
    assertThat(existing.getBasePointInTime()).isEqualTo(LocalDate.of(2024, 12, 2));
  }
}
