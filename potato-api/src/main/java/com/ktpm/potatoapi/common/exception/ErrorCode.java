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
    MESSAGE_KEY_INVALID(1002, "Invalid message key", HttpStatus.BAD_REQUEST),
    BIND_INVALID(1003, "Binding error occurred", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Authentication is required", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You do not have permission to access", HttpStatus.FORBIDDEN),
    MUST_BE_OWNED_OF_CURRENT_MERCHANT(1006,
            "Merchant Admin does not own the current merchant", HttpStatus.BAD_REQUEST),

    // AUTH ERROR
    EMAIL_BLANK(2001, "Email is required", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(2002, "Email is not well-formed", HttpStatus.BAD_REQUEST),
    PASSWORD_BLANK(2003, "Password is required", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN_INVALID(2004,
            "Password must be 8-20 characters long and contain at least one uppercase, one lowercase, one digit, and one special character",
            HttpStatus.BAD_REQUEST),
    FULL_NAME_BLANK(2005, "Full name is required", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD(2006, "New password must be different from current password", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(2007, "Invalid credentials", HttpStatus.BAD_REQUEST),

    // USER ERROR,
    USER_EXISTED(3001, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(3002, "User not existed", HttpStatus.NOT_FOUND),

    // MERCHANT ERROR,
    MERCHANT_EXISTED(4001, "Merchant already existed", HttpStatus.BAD_REQUEST),
    MERCHANT_NOT_FOUND(4002, "Merchant not existed", HttpStatus.NOT_FOUND),
    REGISTERED_MERCHANT_EXISTED(4003, "Registered merchant already existed", HttpStatus.BAD_REQUEST),
    REGISTERED_MERCHANT_NOT_FOUND(4004, "Registered merchant not existed", HttpStatus.NOT_FOUND),
    REGISTERED_MERCHANT_STATUS_NOT_PENDING(4005,
            "Merchant registration is not pending", HttpStatus.BAD_REQUEST),
    REGISTERED_MERCHANT_ADMIN_FULL_NAME_BLANK(4006,
            "Registered merchant admin full name is required", HttpStatus.BAD_REQUEST),
    REGISTERED_MERCHANT_NAME_BLANK(4007, "Registered merchant name is required", HttpStatus.BAD_REQUEST),
    ADDRESS_BLANK(4008, "Merchant address is required", HttpStatus.BAD_REQUEST),
    OPENING_HOURS_EMPTY(4009, "Merchant opening hours are required", HttpStatus.BAD_REQUEST),
    CUISINE_TYPES_EMPTY(4010, "Merchant cuisine types are required", HttpStatus.BAD_REQUEST),
    INTRO_BLANK(4011, "Merchant introduction is required", HttpStatus.BAD_REQUEST),
    MERCHANT_CLOSED(4012, "This merchant is closed now", HttpStatus.BAD_REQUEST),

    // CUISINE TYPE ERROR,
    CUISINE_TYPE_EXISTED(5001, "Cuisine type already existed", HttpStatus.BAD_REQUEST),
    CUISINE_TYPE_NOT_FOUND(5002, "Cuisine type not existed", HttpStatus.NOT_FOUND),
    CUISINE_TYPE_NAME_BLANK(5003, "Cuisine type name is required", HttpStatus.BAD_REQUEST),
    CUISINE_TYPE_IMG_NULL(5004, "Cuisine type image file is required", HttpStatus.BAD_REQUEST),

    // CATEGORY ERROR
    CATEGORY_EXISTED(6001, "Category already existed in this merchant", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(6002, "Category not existed in this merchant", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_BLANK(6003, "Category name is required", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
