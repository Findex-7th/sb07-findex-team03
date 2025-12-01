package com.team3.findex.synclog.dto;

public record AutoSyncConfigDto(
        Long id,
        Long indexInfoId,
        String indexClassification,
        String indexName,
        boolean enabled
){

}
