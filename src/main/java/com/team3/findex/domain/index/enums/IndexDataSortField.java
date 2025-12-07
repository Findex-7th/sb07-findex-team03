package com.team3.findex.domain.index.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IndexData 정렬 필드 Enum
 * <p>
 * API 요청 시 카멜케이스로 받아서 Enum으로 변환합니다.
 * </p>
 */
public enum IndexDataSortField {
    BASE_DATE("baseDate"),              // 기준 일자
    MARKET_PRICE("marketPrice"),        // 시가
    CLOSING_PRICE("closingPrice"),      // 종가
    HIGH_PRICE("highPrice"),            // 고가
    LOW_PRICE("lowPrice"),              // 저가
    VERSUS("versus"),                   // 전일 대비 등락
    FLUCTUATION_RATE("fluctuationRate"), // 등락률
    TRADING_QUANTITY("tradingQuantity"), // 거래량
    TRADING_PRICE("tradingPrice"),      // 거래대금
    MARKET_TOTAL_AMOUNT("marketTotalAmount"), // 상장 시가 총액
    ID("id");                           // ID (기본 정렬용)

    private final String fieldName;

    IndexDataSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 실제 DB 필드명 반환 (Sort 생성에 사용)
     *
     * @return DB 필드명 (카멜케이스)
     */
    @JsonValue  // JSON 직렬화 시 이 값을 사용
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 카멜케이스 문자열을 Enum으로 변환
     *
     * @param value "baseDate", "marketPrice" 등
     * @return 해당하는 IndexDataSortField enum
     */
    @JsonCreator  // JSON 역직렬화 시 이 메서드 사용
    public static IndexDataSortField fromString(String value) {
        if (value == null || value.isBlank()) {
            return BASE_DATE;  // 기본값: 날짜순 정렬
        }

        for (IndexDataSortField field : values()) {
            if (field.fieldName.equalsIgnoreCase(value)) {
                return field;
            }
        }

        // 잘못된 값이면 기본값 반환
        return BASE_DATE;
    }
}
