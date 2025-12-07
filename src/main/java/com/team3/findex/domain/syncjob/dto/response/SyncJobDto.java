package com.team3.findex.domain.syncjob.dto.response;


import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;

public record SyncJobDto(
        Long id,
        JobType jobType,
        Long indexInfoId,
        String targetDate,
        String worker,
        String jobTime,
        Result result
) {
}
