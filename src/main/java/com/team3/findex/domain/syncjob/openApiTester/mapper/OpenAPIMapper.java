package com.team3.findex.domain.syncjob.openApiTester.mapper;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.openApiTester.dto.ApiResponseDto;
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

//    @Mapping(target = "indexInfo", ignore = true)
//    @Mapping(target = "baseDate", source = "basDt", qualifiedByName = "stringToLocalDate")
//    @Mapping(target = "marketPrice", source = "mkp")
//    @Mapping(target = "closingPrice", source = "clpr")
//    @Mapping(target = "highPrice", source = "hipr")
//    @Mapping(target = "lowPrice", source = "lopr")
//    @Mapping(target = "tradingQuantity", source = "trqu")
//    @Mapping(target = "versus", source = "vs")
//    @Mapping(target = "fluctuationRate", source = "fltRt")
//    @Mapping(target = "tradingPrice", source = "trPrc")
//    @Mapping(target = "marketTotalAmount", source = "lstgMrktTotAmt")
//    @Mapping(target = "sourceType", constant = "OPEN_API")
//    IndexData toIndexDataEntity(ApiResponseDto.ApiItemDto dto);

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
