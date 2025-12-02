package com.team3.findex.domain.syncjob.dto;

import java.util.List;

public record IndexDataSyncRequest(
        List<Long> indexInfoIds,
        String baseDateFrom,
        String baseDateTo
) {
}
