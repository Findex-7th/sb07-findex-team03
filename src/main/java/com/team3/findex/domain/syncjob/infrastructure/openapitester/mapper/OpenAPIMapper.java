package com.team3.findex.domain.syncjob.infrastructure.openapitester.mapper;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.infrastructure.openapitester.dto.ApiResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpenAPIMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "indexName", source = "idxNm")
    @Mapping(target = "indexClassification", source = "idxCsf")
    @Mapping(target = "employedItemsCount", source = "epyItmsCnt")
    @Mapping(target = "basePointInTime", source = "basPntm", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "baseIndex", source = "basIdx")
    @Mapping(target = "favorite", constant = "false")
    @Mapping(target = "sourceType", constant = "OPEN_API")
    IndexInfo toIndexInfoEntity(ApiResponseDto.ApiItemDto dto);

    @Mapping(target = "indexInfo", source = "indexInfo")
    @Mapping(target = "baseDate", source = "dto.basDt", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "marketPrice", source = "dto.mkp")
    @Mapping(target = "closingPrice", source = "dto.clpr")
    @Mapping(target = "highPrice", source = "dto.hipr")
    @Mapping(target = "lowPrice", source = "dto.lopr")
    @Mapping(target = "tradingQuantity", source = "dto.trqu")
    @Mapping(target = "versus", source = "dto.vs")
    @Mapping(target = "fluctuationRate", source = "dto.fltRt")
    @Mapping(target = "tradingPrice", source = "dto.trPrc")
    @Mapping(target = "marketTotalAmount", source = "dto.lstgMrktTotAmt")
    @Mapping(target = "sourceType", constant = "OPEN_API")
    IndexData toIndexDataEntity(ApiResponseDto.ApiItemDto dto, IndexInfo indexInfo);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (Exception e) {
            return null;
        }
    }
}
