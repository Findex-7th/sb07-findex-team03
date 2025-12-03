package com.team3.findex.domain.syncjob.dto;

public record AutoSyncConfigDto(
        Long id,
        Long indexInfoId,
        String indexClassification,
        String indexName,
        boolean enabled
){

}
