package com.team3.findex.domain.index.dto;

import com.team3.findex.domain.index.enums.IndexDataSortField;
import org.springframework.data.domain.Sort;

public record IndexDataFindSort(
        IndexDataSortField sortField,
        Sort.Direction order) {
}
