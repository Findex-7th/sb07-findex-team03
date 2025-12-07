package com.team3.findex.domain.index.dto.request;

import com.team3.findex.domain.index.enums.IndexInfoSortField;
import org.springframework.data.domain.Sort;

public record IndexInfoCursorRequest(
        String indexClassification,
        String indexName,
        Boolean favorite,
        Long idAfter,
        Long cursor,
        IndexInfoSortField indexInfoSortField,
        Sort.Direction sortDirection,
        Integer size
) {
}
