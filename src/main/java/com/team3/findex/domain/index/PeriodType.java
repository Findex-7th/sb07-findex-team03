package com.team3.findex.domain.index;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PeriodType {
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY");

    private final String value;

    PeriodType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}


