package kbo.today.common.exception;

public class WeatherApiException extends BusinessException {

    public WeatherApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public WeatherApiException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public WeatherApiException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
