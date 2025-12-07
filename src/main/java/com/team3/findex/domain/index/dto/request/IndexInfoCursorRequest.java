package com.team3.findex.domain.index.dto.request;

import com.team3.findex.domain.index.enums.SortField;
import org.springframework.data.domain.Sort;

public record IndexInfoCursorRequest(
        String indexClassification,
        String indexName,
        Boolean favorite,
        Long idAfter,
        Long cursor,
        SortField sortField,
        Sort.Direction sortDirection,
        Integer size
) {
}
