package com.team3.findex.synclog.enums;

public enum JobType {
    INDEX_INFO("지수 정보"),
    INDEX_DATA("지수 데이터");

    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
