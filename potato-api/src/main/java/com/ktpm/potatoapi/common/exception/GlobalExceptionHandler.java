package com.ktpm.potatoapi.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // xử lí những lỗi chưa xác định được (chưa catch)
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest httpRequest) {
        log.error("Exception: ", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.UNCATEGORIZED.getCode())
                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getStatusCode()).body(errorResponse);
    }

    // xử lí những lỗi chung (not found, existed,..)
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handleAppException(AppException e, HttpServletRequest httpRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(errorResponse);
    }

    // xử lí lỗi gửi JSON sai định dạng, k parse vào object được
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpServletRequest httpRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.JSON_INVALID.getCode())
                .message(ErrorCode.JSON_INVALID.getMessage())
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.status(ErrorCode.JSON_INVALID.getStatusCode()).body(errorResponse);
    }
}
