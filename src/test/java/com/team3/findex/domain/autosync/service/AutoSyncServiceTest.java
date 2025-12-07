package com.team3.findex.domain.autosync.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.team3.findex.domain.autosync.dto.AutoSyncConfigDto;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.enums.SourceType;
import com.team3.findex.domain.autosync.repository.AutoSyncRepository;
import com.team3.findex.domain.index.repository.IndexInfoRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {"open-api.key=DUMMY_KEY", "open-api.url=http://dummy-url.com"})
class AutoSyncServiceTest {

  @Autowired
  private AutoSyncService autoSyncService;

  @Autowired
  private AutoSyncRepository autoSyncRepository;

  @Autowired
  private IndexInfoRepository indexInfoRepository;

  private IndexInfo 코스피;
  private IndexInfo 나스닥;
  private IndexInfo 다우존스;
  private IndexInfo 국고채;
  private IndexInfo 환율지수;

  @BeforeEach
  void setUp() {
    코스피 = createIndexInfo("경제지수", "코스피", 100, 1000.0, SourceType.OPEN_API);
    나스닥 = createIndexInfo("해외주식", "나스닥", 50, 5000.0, SourceType.USER);
    다우존스 = createIndexInfo("해외주식", "다우존스", 30, 3000.0, SourceType.OPEN_API);
    국고채 = createIndexInfo("채권", "국고채", 10, 100.0, SourceType.USER);
    환율지수 = createIndexInfo("환율", "원달러환율", 1, 1200.0, SourceType.OPEN_API);
  }
  private IndexInfo createIndexInfo(String classification, String name,
      int count, double baseIndex, SourceType sourceType) {
    return new IndexInfo(
        null,
        classification,
        name,
        count,
        LocalDate.of(2020, 1, 1),
        baseIndex,
        false,
        sourceType
    );
  }

  private AutoSyncConfigDto saveAndCreateAutoSync(IndexInfo indexInfo) {
    IndexInfo saved = indexInfoRepository.save(indexInfo);
    return autoSyncService.create(saved);
  }

  @Test
  @DisplayName("AutoSync 생성 테스트")
  void createAutoSync() {
      // given
    IndexInfo save = indexInfoRepository.save(코스피);

    // when
    AutoSyncConfigDto result = autoSyncService.create(save);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isNotNull();
    assertThat(result.indexInfoId()).isEqualTo(save.getId());
    assertThat(result.indexName()).isEqualTo(save.getIndexName());
    assertThat(result.indexClassification()).isEqualTo(save.getIndexClassification());
    assertThat(result.enabled()).isFalse();
  }
}