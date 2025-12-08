package com.team3.findex.domain.index.mapper;

import com.team3.findex.domain.index.dto.IndexChartDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.team3.findex.domain.index.IndexChart;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexChartMapper {
    IndexChartDto toDTO(IndexChart indexChart);
}
