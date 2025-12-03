package com.team3.findex.mapper;

import com.team3.findex.dto.indexDataDto.IndexChartDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.team3.findex.entity.index.IndexChart;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexChartMapper {
    IndexChartDto toDTO(IndexChart indexChart);
}
