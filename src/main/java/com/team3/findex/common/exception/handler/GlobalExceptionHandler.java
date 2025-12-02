package com.team3.findex.common.exception.handler;

import com.team3.findex.common.exception.CustomException;
import com.team3.findex.common.exception.ErrorCode;
import com.team3.findex.common.exception.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        log.error("CustomException: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        String detail = e.getDetailMessage() != null ? e.getDetailMessage() : errorCode.getDetails();

        ErrorResponse response = ErrorResponse.error(
                errorCode.getStatus(),
                errorCode.getMessage(),
                detail
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e){
        log.error("EntityNotFoundException : {}", e.getMessage());
        ErrorResponse response = ErrorResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "EntityNotFoundException",
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e){
        log.error("IllegalArgumentException : {}", e.getMessage());
        ErrorResponse response = ErrorResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "IllegalArgumentException",
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

}