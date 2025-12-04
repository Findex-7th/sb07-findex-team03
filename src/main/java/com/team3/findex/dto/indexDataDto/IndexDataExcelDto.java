package com.team3.findex.dto.indexDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexDataExcelDto(
    LocalDate baseDate, // 기준일자
    BigDecimal marketPrice, // 시가
    BigDecimal closingPrice, //	종가
    BigDecimal highPrice, //	고가
    BigDecimal lowPrice, //	저가
    BigDecimal versus, //	전일 대비 등락폭
    BigDecimal fluctuationRate, //	등락률
    BigDecimal tradingQuantity, // 거래량
    BigDecimal tradingPrice, // 거래대금
    BigDecimal marketTotalAmount // 상장시가총액
) {
}
