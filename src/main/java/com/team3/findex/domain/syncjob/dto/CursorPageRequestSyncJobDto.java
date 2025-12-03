package com.team3.findex.domain.syncjob.dto;

import com.team3.findex.synclog.syncjobenums.JobType;

public record CursorPageRequestSyncJobDto(
    JobType jobType,
    Long indexInfoId,
    String baseDateFrom,
    String baseDateTo,
    String worker,
    String jobTimeFrom,
    String jobTimeTo,
    String status,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    int size
) {
}
