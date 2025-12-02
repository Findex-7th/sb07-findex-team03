package com.team3.findex.service.Interface;

import com.team3.findex.domain.index.ChartPeriodType;
import com.team3.findex.dto.indexDataDto.CursorPageResponse;

import com.team3.findex.dto.indexDataDto.ExportCsvRequest;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import com.team3.findex.dto.indexDataDto.IndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface IndexDataServiceInterface {
    CursorPageResponse<IndexDataDto> getAllIndexData(String sortField, String sortDirection, Integer size);
    void createIndexData(IndexDataCreateRequest request);
    void deleteIndexData(Long id);
    IndexDataDto updateIndexData(Long id, IndexDataUpdateRequest request);
    IndexChartDto getChartData(Long id, ChartPeriodType chartPeriodType);
    RankedIndexPerformanceDto performaceRank(long indexInfoId, String periodType, int limit);
    IndexPerformanceDto performaceFavorite(ChartPeriodType chartPeriodType);
    void exportCsv(ExportCsvRequest request);
}
