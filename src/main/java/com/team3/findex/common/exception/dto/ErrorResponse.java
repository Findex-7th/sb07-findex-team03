package com.team3.findex.common.exception.dto;

import com.team3.findex.common.exception.ErrorCode;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String details
){
    public static ErrorResponse error(int status, String message, String details) {
        return new ErrorResponse(LocalDateTime.now(), status, message, details);
    }

    public static ErrorResponse error(ErrorCode errorCode) {
        return error(errorCode.getStatus(), errorCode.getMessage(), errorCode.getDetails());
    }
}