package com.team3.findex.domain.autosync.service;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.autosync.dto.AutoSyncConfigDto;
import com.team3.findex.domain.autosync.dto.AutoSyncConfigUpdateRequest;
import com.team3.findex.domain.autosync.dto.CursorPageRequestAutoSyncConfigDto;
import com.team3.findex.domain.autosync.dto.CursorPageResponseAutoSyncConfigDto;
import com.team3.findex.domain.autosync.mapper.AutoSyncMapper;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.repository.AutoSyncRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 자동 연동 설정 비즈니스 로직 클래스
 * <p>자동 연동 설정의 생성, 수정, 조회, 자동화 제공
 * </p>
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoSyncService {

  private final AutoSyncRepository autoSyncRepository;
  private final AutoSyncMapper autoSyncMapper;

  /**
   * 지수 정보 기반 자동 연동 설정 생성
   * 초기 상태 비활성화로 설정, 동일한 지수 설정이 존재하는 경우 예외 발생
   * @param indexInfo 연동 대상 지수 정보
   * @return 생성된 자동 연동 설정 DTO
   * @throws IllegalArgumentException 지수 정보가 null이거나 이미 존재하는 경우
   */
  @Transactional
  public AutoSyncConfigDto create(IndexInfo indexInfo){

    if(indexInfo == null){
      throw new IllegalArgumentException("지수 정보가 없습니다.");
    }
    if(autoSyncRepository.existsByIndexInfo(indexInfo))
      throw new IllegalArgumentException("자동 연동 설정이 이미 되어있습니다.");

    AutoSync autoSync = new AutoSync(indexInfo);
    autoSyncRepository.save(autoSync);
    return autoSyncMapper.toDto(autoSync);
  }

  /**
   * 자동 연동 설정 활성화 여부 메서드
   * @param id 수정할 자동 연결 설정 ID
   * @param enable 활성화 정보 담은 DTO
   * @return 수정된 자동 연결 설정 DTO
   * @throws IllegalArgumentException 해당 ID의 설정이 존재하지 않는 경우
   */
  @Transactional
  public AutoSyncConfigDto updateEnable(Long id, AutoSyncConfigUpdateRequest enable){
    AutoSync autoSync = autoSyncRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("자동 설정할 ID가 없습니다."));
    autoSync.updateEnable(enable.enabled());
    return autoSyncMapper.toDto(autoSync);
  }

  public CursorPageResponseAutoSyncConfigDto getAutoSyncConfig(
      CursorPageRequestAutoSyncConfigDto request
  ) {
    Long indexInfoId = request.indexInfoId();
    boolean enabled = request.enabled();
    Long idAfter = request.idAfter();
    String sortField = request.sortField() != null ? request.sortField() : "id";
    String sortDirectionValue = request.sortDirection() != null ? request.sortDirection() : "ASC";
    int size = request.size();

    List<AutoSync> results = autoSyncRepository.findWithCursor(
        indexInfoId,
        enabled,
        idAfter,
        sortField,
        sortDirectionValue,
        size
    );

    boolean hasNext = results.size() > size;
    if (hasNext) {
      results = results.subList(0, size);
    }

    Long nextIdAfter = results.isEmpty()
        ? null : results.get(results.size() - 1).getId();

    String nextCursor = nextIdAfter != null
        ? String.valueOf(nextIdAfter)
        : null;

    List<AutoSyncConfigDto> content = autoSyncMapper.toDtoList(results);

    long totalElements = autoSyncRepository.countWithFilter(indexInfoId, enabled);

    return new CursorPageResponseAutoSyncConfigDto(
        content, nextCursor, nextIdAfter, size, totalElements, hasNext);
  }


}
