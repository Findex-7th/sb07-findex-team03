package com.team3.findex.domain.autosync.service;

import com.team3.findex.common.exception.CustomException;
import com.team3.findex.common.exception.ErrorCode;
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
      throw new CustomException(ErrorCode.AUTO_SYNC_CONFIG_NOT_FOUND);
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
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
    autoSync.updateEnable(enable.enabled());
    return autoSyncMapper.toDto(autoSync);
  }

  /**
   * 자동 연동 설정 목록 조회
   * <p>설정 목록 조회하고, 페이지 응답 DTO 변환</p>
   * <oi>
   *   <li>요청 파라미터 기본 값 적용</li>
   *   <li>실제 개수보다 1개 더 조회</li>
   *   <li>조회 결과가 요청 size보다 많으면 다음 페이지가 있음</li>
   *   <li>다음 페이지 조회를 위해서 커서 정보 생성</li>
   *   <li>Entity를 DTO 변환하여 응답 생성</li>
   * </oi>
   * @param request 페이지 요청 정보
   * @return reponse 페이지 요청 응답
   */
  @Transactional
  public CursorPageResponseAutoSyncConfigDto getAutoSyncConfig(
      CursorPageRequestAutoSyncConfigDto request
  ) {
    Long indexInfoId = request.indexInfoId();
    boolean enabled = request.enabled();
    Long idAfter = request.idAfter();
    String cursor = request.cursor();
    String sortField = request.sortField() != null ? request.sortField() : "indexInfo.indexName";
    String sortDirectionValue = request.sortDirection() != null ? request.sortDirection() : "ASC";
    int size = request.size();

    List<AutoSync> results = autoSyncRepository.findWithCursor(
        indexInfoId,
        enabled,
        idAfter,
        cursor,
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

    String nextCursor = results.isEmpty()
        ? null : results.get(results.size() - 1).getIndexInfo().getIndexName();

    List<AutoSyncConfigDto> content = autoSyncMapper.toDtoList(results);

    long totalElements = autoSyncRepository.countWithFilter(indexInfoId, enabled);

    return new CursorPageResponseAutoSyncConfigDto(
        content, nextCursor, nextIdAfter, size, totalElements, hasNext);
  }


}
