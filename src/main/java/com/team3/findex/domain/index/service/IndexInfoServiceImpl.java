package com.team3.findex.domain.index.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.mapper.IndexInfoMapper;
import com.team3.findex.domain.index.test.AutoSyncConfig;
import com.team3.findex.domain.index.test.AutoSyncConfigRepository;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoServiceImpl implements IndexInfoService {
  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;
  private final IndexDataRepository indexDataRepository;

  private final AutoSyncConfigRepository autoSyncConfigRepository;

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
        request.basePointInTime(),
        request.baseIndex(),
        request.favorite() != null && request.favorite(),
        SourceType.USER
    );

    IndexInfo saved = indexInfoRepository.save(indexInfo);

    AutoSyncConfig config = new AutoSyncConfig(
        saved.getId(),
        saved.getIndexClassification(),
        saved.getIndexName(),
        true
    );
    autoSyncConfigRepository.save(config);

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

    indexInfo.update(
        request.employedItemsCount(),
        request.basePointInTime(),
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
        .orElseThrow(()-> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));
    return indexInfoMapper.toDto(indexInfo);
//    return new IndexInfoDto(
//        entity.getId(),
//        entity.getIndexClassification(),
//        entity.getIndexName(),
//        entity.getEmployedItemsCount(),
//        entity.getBasePointInTime(),
//        entity.getBaseIndex(),
//        entity.getSourceType(),
//        entity.getFavorite()
//    );
  }

  @Override
  public List<IndexInfoDto> findAll() {
    return indexInfoMapper.toDtoList(indexInfoRepository.findAll());

//    return indexInfoRepository.findAll().stream()
//        .map(entity->new IndexInfoDto(
//            entity.getId(),
//            entity.getIndexClassification(),
//            entity.getIndexName(),
//            entity.getEmployedItemsCount(),
//            entity.getBasePointInTime(),
//            entity.getBaseIndex(),
//            entity.getSourceType(),
//            entity.getFavorite()
//        ))
//        .toList();
  }

  @Override
  @Transactional
  public void delete(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));

    indexDataRepository.deleteById(indexInfo.getId());
    indexInfoRepository.delete(indexInfo);
  }
}