package com.team3.findex.domain.index.dto;

import com.team3.findex.domain.index.enums.SortField;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Sort;

public record IndexInfoFindSort(
        SortField sortField,
        Sort.Direction sortDirection
) {
}
