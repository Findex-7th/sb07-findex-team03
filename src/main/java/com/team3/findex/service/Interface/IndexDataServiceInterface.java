package com.team3.findex.service.Interface;

import com.team3.findex.domain.index.dto.request.IndexDataCursorRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexDataDto;
import com.team3.findex.dto.indexDataDto.CursorPageResponse;

import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataWithInfoDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.PeriodType;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IndexDataServiceInterface {
    IndexDataDto createIndexData(IndexDataCreateRequest request);
    void deleteIndexData(Long id);
    IndexDataDto updateIndexData(Long id, IndexDataUpdateRequest request);
    List<IndexDataWithInfoDto> favoriteIndex(PeriodType periodType);
    IndexChartDto getChartData(Long id, PeriodType periodType);
    List<RankedIndexPerformanceDto> performanceRank(Long indexInfoId, PeriodType periodType, int limit);
    void exportCsv( Long indexInfoId,
        String startDate,
        String endDate,
        String sortField,
        String sortDirection,
        HttpServletResponse response) throws IOException;

    CursorPageResponseIndexDataDto getAllIndexData(IndexDataCursorRequest indexDataCursorRequest);
}

