package com.team3.findex.domain.index.dto.request;

import com.team3.findex.domain.index.enums.IndexDataSortField;
import com.team3.findex.domain.index.enums.IndexInfoSortField;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public record IndexDataCursorRequest(
        Long indexInfoId,
        LocalDate startDate,
        LocalDate endDate,
        Long idAfter,
        Long cursor,
        IndexDataSortField sortField,
        Sort.Direction order,
        Integer size
) {
}
