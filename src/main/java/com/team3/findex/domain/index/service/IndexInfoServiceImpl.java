package com.team3.findex.domain.index.service;

import com.team3.findex.common.openapi.OpenApiProvider;
import com.team3.findex.domain.autosync.service.AutoSyncService;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoSummaryDto;
import com.team3.findex.domain.index.mapper.IndexInfoMapper;
import com.team3.findex.repository.AutoSyncRepository;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;
  private final IndexDataRepository indexDataRepository;
  private final AutoSyncService autoSyncService;
  private final OpenApiProvider openApiProvider;
  private final AutoSyncRepository autoSyncRepository;

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

    indexDataRepository.deleteByIndexInfoId(indexInfo.getId());
    autoSyncRepository.deleteByIndexInfoId(indexInfo.getId());
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

  // 페이지네이션(정렬 생성 메서드 추가)
  private Sort createSort(String sortKey) {
    if (sortKey == null) return Sort.by("id").ascending();

    return switch (sortKey) {
      case "indexClassification" -> Sort.by("indexClassification").ascending();
      case "indexName" -> Sort.by("indexName").ascending();
      case "employedItemsCount" -> Sort.by("employedItemsCount").ascending();
      default -> Sort.by("id").ascending();
    };
  }

  // 페이지네이션(search 메서드 전체)
  @Override
  public CursorPageResponseIndexInfoDto search(
      String classification,
      String name,
      Boolean favorite,
      String sortKey,
      Long cursorId,
      int size
  ) {
    Sort sort = createSort(sortKey);
    Pageable pageable = PageRequest.of(0, size + 1, sort);

    List<IndexInfo> result = indexInfoRepository.searchWithCursor(
        classification,
        name,
        favorite,
        cursorId,
        pageable
    );

    boolean hasNext = false;

    if (result.size() > size) {
      hasNext = true;
      result.remove(size);
    }

    List<IndexInfoDto> dtoList = result.stream()
        .map(indexInfoMapper::toDto)
        .toList();

    Long nextCursor = result.isEmpty()
        ? null
        : result.get(result.size() - 1).getId();

    return new CursorPageResponseIndexInfoDto(dtoList, nextCursor, hasNext);
  }

  @Override
  public List<IndexInfoSummaryDto> getSummaryList() {
    return indexInfoMapper.toSummaryDtoList(indexInfoRepository.findAll());
  }


}