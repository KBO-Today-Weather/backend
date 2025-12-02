package kbo.today.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, String> fieldErrors;

    private ErrorResponse(String code, String message, Map<String, String> fieldErrors) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getDefaultMessage(), null);
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return new ErrorResponse(errorCode.getCode(), customMessage, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, Map<String, String> fieldErrors) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getDefaultMessage(), fieldErrors);
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage, Map<String, String> fieldErrors) {
        return new ErrorResponse(errorCode.getCode(), customMessage, fieldErrors);
    }
}

