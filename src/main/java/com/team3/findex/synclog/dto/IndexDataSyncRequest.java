package com.team3.findex.synclog.dto;

import java.util.List;

public record IndexDataSyncRequest(
        List<Long> indexInfoIds,
        String baseDateFrom,
        String baseDateTo
) {
}
