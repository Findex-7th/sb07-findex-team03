package com.team3.findex.domain.index.service;

import com.team3.findex.domain.index.dto.IndexChartDto;
import com.team3.findex.domain.index.dto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.enums.ChartPeriodType;
import com.team3.findex.domain.index.enums.PeriodType;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IndexChartService {

    IndexChartDto getChartData(Long id, ChartPeriodType periodType);
    List<RankedIndexPerformanceDto> performanceRank(Long indexInfoId, PeriodType periodType, int limit);
}
