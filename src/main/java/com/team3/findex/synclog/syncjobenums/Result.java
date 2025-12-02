package com.team3.findex.synclog.syncjobenums;

public enum Result {
    SUCCESS("성공"),
    FAILED("실패");

    private final String description;

    Result(String description) {
        this.description = description;
    }
}
