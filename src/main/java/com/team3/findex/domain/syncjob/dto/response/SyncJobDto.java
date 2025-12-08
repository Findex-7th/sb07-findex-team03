package com.team3.findex.domain.syncjob.dto.response;


import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;

import java.time.Instant;
import java.time.LocalDateTime;

public record SyncJobDto(
        Long id,
        JobType jobType,
        Long indexInfoId,
        String targetDate,
        String worker,
        Instant jobTime,
        Result result
) {
}
