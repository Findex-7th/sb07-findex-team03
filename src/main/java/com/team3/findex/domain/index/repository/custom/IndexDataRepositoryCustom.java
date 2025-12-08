package com.team3.findex.domain.index.repository.custom;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.dto.*;
import com.team3.findex.domain.index.enums.ChartPeriodType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface IndexDataRepositoryCustom {
    List<IndexData> findByCondition(Long idAfter,
                                    IndexDataFindCondition condition,
                                    int size,
                                    IndexDataFindSort sort);

    Long CountByCondition(IndexDataFindCondition condition);


    List<ChartDataPointDto> findChartData(Long indexInfoId, ChartPeriodType periodType, int avgAmount);
}

