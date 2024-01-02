package savemyreceipt.server.exception.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import savemyreceipt.server.exception.ErrorStatus;

@Getter
@Slf4j
public class CustomException extends RuntimeException{
    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
        log.error("errorStatus : {}, message : {}", errorStatus, message);
    }

    public Integer getHttpStatus() {
        return errorStatus.getHttpStatusCode();
    }
}

