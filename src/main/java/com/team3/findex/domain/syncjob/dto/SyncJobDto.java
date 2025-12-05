package com.team3.findex.domain.syncjob.dto;


import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;

import java.time.Instant;

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
