package com.team3.findex.common.openapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 지수 데이터 연동 데이터(openApi)
 * <p>
 * Open API에서 가져온 지수 데이터 담는 DTO,
 * 지수 데이터 등록 및 수정에 사용
 * </p>
 * @param baseDate 기준일자
 * @param marketPrice 시가
 * @param closingPrice 종가
 * @param highPrice 고가
 * @param lowPrice 저가
 * @param tradingQuantity 거래량
 * @param versus 전일 대비 등락
 * @param fluctuationRate 등락률
 * @param tradingPrice 거래대금
 * @param marketTotalAmount 상장 시가 총액
 */
public record IndexSyncData(

    LocalDate baseDate,
    BigDecimal marketPrice,
    BigDecimal closingPrice,
    BigDecimal highPrice,
    BigDecimal lowPrice,
    Long tradingQuantity,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    Long tradingPrice,
    Long marketTotalAmount
) {}
