package com.ktpm.potatoapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // GLOBAL ERROR
    UNCATEGORIZED(9999, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    JSON_INVALID(1001, "Invalid JSON request", HttpStatus.BAD_REQUEST),

    // CUISINE TYPE ERROR
    CUISINE_TYPE_EXISTED(5001, "Cuisine type already existed", HttpStatus.BAD_REQUEST),
    CUISINE_TYPE_NOT_FOUND(5002, "Cuisine type not existed", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatusCode statusCode;
}
