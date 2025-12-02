package com.team3.findex.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 400 BAD REQUEST
    INVALID_INPUT_VALUE(400, "IllegalArgumentException", "잘못된 요청입니다."),

    // 404 NOT FOUND
    // 지수 정보
    INDEX_INFO_NOT_FOUND(404, "EntityNotFoundException", "지수 정보를 찾을 수 없습니다."),

    // 지수 데이터
    INDEX_DATA_NOT_FOUND(404, "EntityNotFoundException", "지수 데이터를 찾을 수 없습니다."),

    // 연동 작업
    SYNC_JOB_NOT_FOUND(404, "EntityNotFoundException", "연동 작업을 찾을 수 없습니다."),

    // 자동 연동 설정
    AUTO_SYNC_CONFIG_NOT_FOUND(404, "EntityNotFoundException", "자동 연동 설정을 찾을 수 없습니다."),

    // 500 SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "Exception", "서버 오류가 발생했습니다.");


    private final int status;
    private final String message;
    private final String details;

    ErrorCode(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
