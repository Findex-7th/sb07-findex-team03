package com.team3.findex.domain.autosync.mapper;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.autosync.dto.AutoSyncConfigDto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * AutoSync 엔티티와 DTO 간 변화하는 Mapper
 * <p>MapStruct를 사용하여 AutoSync 엔티티를 AutoSyncConfigDto로 자동 변환</p>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AutoSyncMapper {

    /**
     * AutoSync 엔티티를 AutoSyncConfigDto 단일 변환
     *
     * @param autoSync 엔티티
     * @return 변환 DTO
     */
    @Mapping(source = "indexInfo.id", target = "indexInfoId")
    @Mapping(source = "indexInfo.indexClassification", target = "indexClassification")
    @Mapping(source = "indexInfo.indexName", target = "indexName")
    @Mapping(source = "enable", target = "enabled")
    AutoSyncConfigDto toDto(AutoSync autoSync);

    /**
     * AutoSync 엔티티를 AutoSyncConfigDto 리스트 변환 - 목록 조회용
     *
     * @param autoSyncs 엔티티
     * @return List 변환 DTO
     */
    List<AutoSyncConfigDto> toDtoList(List<AutoSync> autoSyncs);

}
