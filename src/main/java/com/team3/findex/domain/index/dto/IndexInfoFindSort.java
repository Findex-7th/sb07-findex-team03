package com.team3.findex.domain.index.dto;

import com.team3.findex.domain.index.enums.IndexInfoSortField;
import org.springframework.data.domain.Sort;

public record IndexInfoFindSort(
        IndexInfoSortField indexInfoSortField,
        Sort.Direction sortDirection
) {
}
