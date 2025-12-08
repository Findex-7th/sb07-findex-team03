package com.team3.findex.domain.index.mapper;

import com.team3.findex.domain.index.dto.request.IndexDataCreateRequest;
import com.team3.findex.domain.index.dto.IndexDataDto;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.dto.IndexDataExcelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexDataMapper {

    @Mapping(source = "indexInfo.id", target = "indexInfoId")
    IndexDataDto toDTO(IndexData indexData);
    IndexData toEntity(IndexDataCreateRequest requestDto);
    IndexDataExcelDto toExcelDto(IndexData indexData);
    List<IndexDataDto> toDtoList(List<IndexData> indexData);
}
