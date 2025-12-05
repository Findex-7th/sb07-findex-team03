package com.team3.findex.domain.index.mapper;

import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.dto.indexDataDto.IndexDataExcelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexDataMapper {

    @Mapping(source = "indexInfo.id", target = "indexInfoId")
    IndexDataDto toDTO(IndexData indexData);
    IndexData toEntity(IndexDataCreateRequest requestDto);
    IndexDataExcelDto toExcelDto(IndexData indexData);

}
