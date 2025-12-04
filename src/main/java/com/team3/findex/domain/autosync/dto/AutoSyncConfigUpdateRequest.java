package com.team3.findex.domain.autosync.dto;

/**
 * 자동 연동 설정 수정 요청 DTO
 */
public record AutoSyncConfigUpdateRequest(
   boolean enabled
) {}
