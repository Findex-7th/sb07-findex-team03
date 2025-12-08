package com.team3.findex.domain.index.service;

import com.team3.findex.domain.index.enums.ChartPeriodType;
import com.team3.findex.domain.index.dto.request.IndexDataCursorRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexDataDto;

import com.team3.findex.domain.index.dto.IndexChartDto;
import com.team3.findex.domain.index.dto.request.IndexDataCreateRequest;
import com.team3.findex.domain.index.dto.IndexDataDto;
import com.team3.findex.domain.index.dto.request.IndexDataUpdateRequest;
import com.team3.findex.domain.index.dto.IndexDataWithInfoDto;
import com.team3.findex.domain.index.dto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.enums.PeriodType;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IndexDataService {
    IndexDataDto createIndexData(IndexDataCreateRequest request);
    void deleteIndexData(Long id);
    IndexDataDto updateIndexData(Long id, IndexDataUpdateRequest request);
    List<IndexDataWithInfoDto> favoriteIndex(PeriodType periodType);
    void exportCsv( Long indexInfoId,
        String startDate,
        String endDate,
        String sortField,
        String sortDirection,
        HttpServletResponse response) throws IOException;

    CursorPageResponseIndexDataDto getAllIndexData(IndexDataCursorRequest indexDataCursorRequest);
}

