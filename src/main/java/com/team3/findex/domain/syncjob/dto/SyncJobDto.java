package com.team3.findex.domain.syncjob.dto;


import com.team3.findex.domain.syncjob.JobType;
import com.team3.findex.domain.syncjob.Result;

public record SyncJobDto(
        Long Id,
        JobType jobType,
        Long indexInfoId,
        String targetDate,
        String worker,
        String jobTime,
        Result result
) {
}
