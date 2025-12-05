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
  public List<IndexInfoDto> findAllSorted(String sortKey, String order) {
    Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort;

    if (sortKey == null || sortKey.isBlank()) {
      sort = Sort.by(direction, "id");
    } else {
      sort = Sort.by(direction, sortKey);
    }

    List<IndexInfo> result = indexInfoRepository.findAll(sort);
    return result.stream()
        .map(indexInfoMapper::toDto)
        .toList();
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
  private Sort createSort(String sortKey, String order) {
    if (sortKey == null || sortKey.isBlank()) {
      return Sort.by("id").ascending();
    }

    Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
    return Sort.by(direction, sortKey);
  }

  // 페이지네이션(search 메서드 전체)
  @Override
  public CursorPageResponseIndexInfoDto search(
      String classification,
      String name,
      Boolean favorite,
      String sortKey,
      String order,
      Long cursorId,
      int size
  ) {
    List<IndexInfo> result = indexInfoRepository.searchWithCursor(
        classification, name, favorite, cursorId
    );

    // ✨ 정렬 적용
    result.sort((a, b) -> {
      Comparable aValue = extractComparable(a, sortKey);
      Comparable bValue = extractComparable(b, sortKey);

      if (aValue == null || bValue == null) return 0;

      int cmp = aValue.compareTo(bValue);
      return "desc".equalsIgnoreCase(order) ? -cmp : cmp;
    });

    // ✨ 페이징
    boolean hasNext = result.size() > size;
    List<IndexInfo> pageResult = hasNext ? result.subList(0, size) : result;

    List<IndexInfoDto> dtoList = pageResult.stream()
        .map(indexInfoMapper::toDto)
        .toList();

    Long nextCursor = pageResult.isEmpty() ? null : pageResult.get(pageResult.size() - 1).getId();

    return new CursorPageResponseIndexInfoDto(dtoList, nextCursor, hasNext);
  }

  // ✨ 정렬 필드 추출 유틸
  private Comparable extractComparable(IndexInfo index, String sortKey) {

    if (sortKey == null || sortKey.isBlank()) {
      return index.getId();   // 기본 정렬 기준
    }

    switch (sortKey) {
      case "indexClassification":
        return index.getIndexClassification();
      case "indexName":
        return index.getIndexName();
      case "employedItemsCount":
        return index.getEmployedItemsCount();
      case "baseIndex":
        return index.getBaseIndex();
      case "basePointInTime":
        return index.getBasePointInTime();
      case "favorite":
        return index.getFavorite();
      case "id":
      default:
        return index.getId();
    }
  }


  @Override
  public List<IndexInfoSummaryDto> getSummaryList(String sortKey, String order) {
    Sort.Direction direction = "desc".equals(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort;

    if( sortKey == null ||sortKey.isEmpty()){
      sort = Sort.by(direction, "id");
    } else {
      sort = Sort.by(direction, sortKey);
    }

    List<IndexInfo> list = indexInfoRepository.findAll(sort);
    return indexInfoMapper.toSummaryDtoList(list);
  }


}