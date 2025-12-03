package com.team3.findex.domain.index.mapper;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexInfoMapper {

  IndexInfoDto toDto(IndexInfo indexInfo);
  List<IndexInfoDto> toDtoList(List<IndexInfo> indexInfos);
}
