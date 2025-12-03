package com.team3.findex.mapper;

import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.entity.index.IndexData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexDataMapper {

    IndexDataDto toDTO(IndexData indexData);
    IndexData toEntity(IndexDataCreateRequest requestDto);
}
