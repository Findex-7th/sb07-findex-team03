package com.team3.findex.synclog.dto;

import com.team3.findex.synclog.syncjobenums.JobType;
import com.team3.findex.synclog.syncjobenums.Result;

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
