package com.team3.findex.domain.syncjob.mapper;

import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.request.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.response.SyncJobDto;
import com.team3.findex.domain.syncjob.dto.search.SyncJobCursorSearch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncJobMapper {

    @Mapping(source = "indexInfo.id", target = "indexInfoId")
    @Mapping(source = "createdAt", target = "jobTime", qualifiedByName = "korTime")
    SyncJobDto toDto(SyncJob syncJob);


    SyncJobCursorSearch toCursorSearch(CursorPageRequestSyncJobDto dto);

    @Named("korTime")
    default String stringToLocalDate(Instant createdAt) {
        return createdAt.plus(9, ChronoUnit.HOURS).toString();
    }

}
