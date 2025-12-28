package kbo.today.common.exception;

public class StadiumNotFoundException extends BusinessException {
    public StadiumNotFoundException() {
        super(ErrorCode.STADIUM_NOT_FOUND);
    }

    public StadiumNotFoundException(String message) {
        super(ErrorCode.STADIUM_NOT_FOUND, message);
    }
}

