package com.team3.findex.domain.autosync.dto;

/**
 * 자동 연동 설정 DTO
 */
public record AutoSyncConfigDto(
        Long id,
        Long indexInfoId,
        String indexClassification,
        String indexName,
        boolean enabled
) {}
