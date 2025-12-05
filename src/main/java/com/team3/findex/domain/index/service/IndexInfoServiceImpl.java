package com.team3.findex.domain.index.service;

import com.team3.findex.common.openapi.OpenApiProvider;
import com.team3.findex.domain.autosync.service.AutoSyncService;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.mapper.IndexInfoMapper;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;
  private final IndexDataRepository indexDataRepository;
  private final AutoSyncService autoSyncService;
  private final OpenApiProvider openApiProvider;

  @Override
  public IndexInfoDto create(IndexInfoCreateRequest request) {
    boolean exists = indexInfoRepository.existsByIndexClassificationAndIndexName(
        request.indexClassification(),
        request.indexName()
    );

    if (exists) {
      throw new IllegalArgumentException("해당 지수 분류명과 지수명 조합은 이미 존재합니다.");
    }

    IndexInfo indexInfo = new IndexInfo(
        request.indexClassification(),
        request.indexName(),
        request.employedItemsCount(),
        LocalDate.parse(request.basePointInTime()),
        request.baseIndex(),
        request.favorite() != null && request.favorite(),
        SourceType.USER
    );

    IndexInfo saved = indexInfoRepository.save(indexInfo);

    // 자동 연동 설정 저장
    autoSyncService.create(indexInfo);
    return indexInfoMapper.toDto(saved);

//    return new IndexInfoDto(
//        saved.getId(),
//        saved.getIndexClassification(),
//        saved.getIndexName(),
//        saved.getEmployedItemsCount(),
//        saved.getBasePointInTime(),
//        saved.getBaseIndex(),
//        saved.getSourceType(),
//        saved.getFavorite()
//    );
  }

  @Override
  public IndexInfoDto update(IndexInfoUpdateRequest request) {
    IndexInfo indexInfo = indexInfoRepository.findById(request.id())
        .orElseThrow(() -> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));

    LocalDate parsedDate = null;
    if (request.basePointInTime() != null) {
      parsedDate = LocalDate.parse(request.basePointInTime());
    }

    indexInfo.update(
        request.employedItemsCount(),
        parsedDate,
        request.baseIndex(),
        request.favorite()
    );

    IndexInfo updated = indexInfoRepository.save(indexInfo);
    return indexInfoMapper.toDto(updated);
//    return new IndexInfoDto(
//        updated.getId(),
//        updated.getIndexClassification(),
//        updated.getIndexName(),
//        updated.getEmployedItemsCount(),
//        updated.getBasePointInTime(),
//        updated.getBaseIndex(),
//        updated.getSourceType(),
//        updated.getFavorite()
//    );
  }

  @Override
  public IndexInfoDto getById(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));
    return indexInfoMapper.toDto(indexInfo);
  }

  @Override
  public List<IndexInfoDto> findAll() {
    return indexInfoMapper.toDtoList(indexInfoRepository.findAll());

  }

  @Override
  @Transactional
  public void delete(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));

    indexDataRepository.deleteById(indexInfo.getId());
    indexInfoRepository.delete(indexInfo);
  }

  @Transactional
  public void autoSyncFromOpenApi() {
    // Open Api에서 오늘 지수 정보 리스트 갖고 옴
    var list = openApiProvider.getIndexInfoDataByDate(LocalDate.now());

    list.forEach(sync -> {
      indexInfoRepository
          .findByIndexClassificationAndIndexName(
              sync.indexClassification(),
              sync.indexName()
          )
          .ifPresentOrElse(
              // 이미 존재하면 자동 수정
              existing -> {
                existing.update(
                    sync.employedItemsCount(),
                    sync.basePointInTime(),
                    sync.baseIndex(),
                    existing.getFavorite()
                );
                indexInfoRepository.save(existing);
              },
              // 존재하지 않으면 자동 등록
              () -> {
                IndexInfo info = new IndexInfo(
                    sync.indexClassification(),
                    sync.indexName(),
                    sync.employedItemsCount(),
                    sync.basePointInTime(),
                    sync.baseIndex(),
                    false,
                    SourceType.OPEN_API
                );
                indexInfoRepository.save(info);
                autoSyncService.create(info);
              }
          );
    });
  }
}