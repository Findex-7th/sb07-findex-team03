package com.team3.findex.domain.index.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IndexInfo 정렬 필드 Enum
 * <p>
 * API 요청 시 카멜케이스로 받아서 Enum으로 변환합니다.
 * </p>
 */
public enum SortField {
    INDEX_CLASSIFICATION("indexClassification"),
    INDEX_NAME("indexName"),
    EMPLOYED_ITEMS_COUNT("employedItemsCount"),
    ID("id");

    private final String fieldName;

    SortField(String fieldName) {
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
     * @param value "indexClassification", "indexName" 등
     * @return 해당하는 SortField enum
     */
    @JsonCreator  // JSON 역직렬화 시 이 메서드 사용
    public static SortField fromString(String value) {
        if (value == null || value.isBlank()) {
            return INDEX_CLASSIFICATION;  // 기본값
        }

        for (SortField field : values()) {
            if (field.fieldName.equalsIgnoreCase(value)) {
                return field;
            }
        }

        // 잘못된 값이면 기본값 반환
        return INDEX_CLASSIFICATION;
    }
}
