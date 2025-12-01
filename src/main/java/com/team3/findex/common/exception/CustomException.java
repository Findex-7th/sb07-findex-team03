package com.team3.findex.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailMessage;

    // 에러 코드만
    public CustomException(ErrorCode errorCode){
        super(errorCode.getDetails());
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    // ErrorCode + 커스텀 메시지
    public CustomException(ErrorCode errorCode, String detailMessage){
        super(detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

}