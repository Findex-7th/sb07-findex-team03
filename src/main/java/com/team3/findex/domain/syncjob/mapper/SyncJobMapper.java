package com.team3.findex.domain.syncjob.mapper;

import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncJobMapper {

    @Mapping(source = "indexInfo.id", target = "indexInfoId")
    @Mapping(source = "indexInfo.basePointInTime", target = "targetDate")
    @Mapping(source = "createdAt", target = "jobTime")
    SyncJobDto toDto(SyncJob syncJob);
}
