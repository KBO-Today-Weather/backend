package kbo.today.common.exception;

public class InvalidStadiumLocationException extends BusinessException {
    public InvalidStadiumLocationException() {
        super(ErrorCode.INVALID_STADIUM_LOCATION);
    }

    public InvalidStadiumLocationException(String message) {
        super(ErrorCode.INVALID_STADIUM_LOCATION, message);
    }
}

