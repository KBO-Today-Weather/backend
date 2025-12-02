package kbo.today.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Authentication & Authorization (AUTH_XXX)
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_001", "Invalid email or password"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_002", "Unauthorized access"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_003", "Token has expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_004", "Invalid token"),

    // User Related (USER_XXX)
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_001", "Email already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "User not found"),
    INVALID_USER_INPUT(HttpStatus.BAD_REQUEST, "USER_003", "Invalid user input"),

    // Validation (VALID_XXX)
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALID_001", "Validation failed"),

    // General (GEN_XXX)
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "GEN_001", "Invalid input"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GEN_500", "An unexpected error occurred");

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String code, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}

