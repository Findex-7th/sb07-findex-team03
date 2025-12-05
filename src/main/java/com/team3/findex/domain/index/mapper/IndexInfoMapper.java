package com.team3.findex.domain.index.mapper;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoSummaryDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexInfoMapper {

  // 상세
  @Mapping(target = "basePointInTime", expression = "java(indexInfo.getBasePointInTime().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE))")
  IndexInfoDto toDto(IndexInfo indexInfo);
  List<IndexInfoDto> toDtoList(List<IndexInfo> indexInfos);

  // 요약
  @Mapping(target = "id", source = "id")
  @Mapping(target = "indexClassification", source = "indexClassification")
  @Mapping(target = "indexName", source = "indexName")
  IndexInfoSummaryDto toSummaryDto(IndexInfo indexInfo);

  List<IndexInfoSummaryDto> toSummaryDtoList(List<IndexInfo> indexInfos);

}
