package com.team3.findex.domain.autosync.controller;

import com.team3.findex.domain.autosync.dto.AutoSyncConfigDto;
import com.team3.findex.domain.autosync.dto.AutoSyncConfigUpdateRequest;
import com.team3.findex.domain.autosync.dto.CursorPageRequestAutoSyncConfigDto;
import com.team3.findex.domain.autosync.dto.CursorPageResponseAutoSyncConfigDto;
import com.team3.findex.domain.autosync.service.AutoSyncService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 자동 연동 설정 활성화 수정 및 조회 API
 */
@RestController
@RequestMapping("/api/auto-sync-configs")
@RequiredArgsConstructor
public class AutoSyncController {

  private final AutoSyncService autoSyncService;

  /**
   * enable 상태를 변경
   * @param id 수정할 자동 연동 설정 Id
   * @param request 활성화 상태 담은 DTO
   * @return 수정된 연동 설정 정보 DTO
   */
  @PatchMapping("/{id}")
  public ResponseEntity<AutoSyncConfigDto> updateEnable(
      @PathVariable Long id,
      @RequestBody AutoSyncConfigUpdateRequest request){
    AutoSyncConfigDto response = autoSyncService.updateEnable(id, request);
    return ResponseEntity.status(200).body(response);
  }


  /**
   * 자동 연동 설정 목록 조회 (커서 기반 페이지네이션)
   * @param request 페이지 요청 정보
   * @return 응답 DTO
   */
  @GetMapping
  public ResponseEntity<CursorPageResponseAutoSyncConfigDto> getAutoSyncConfig(
      @ParameterObject CursorPageRequestAutoSyncConfigDto request) {
    CursorPageResponseAutoSyncConfigDto response = autoSyncService.getAutoSyncConfig(request);
    return ResponseEntity.status(200).body(response);
  }
}
