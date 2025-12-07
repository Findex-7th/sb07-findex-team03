package com.team3.findex.domain.syncjob.dto.search;

import com.team3.findex.domain.syncjob.enums.JobType;

public record SyncJobCursorSearch(
        JobType jobType,
        Long indexInfoId,
        String baseDateFrom,
        String baseDateTo,
        String worker,
        String jobTimeFrom,
        String jobTimeTo,
        String status,
        String cursor,
        String sortField,
        String sortDirection,
        int size

) {
}
