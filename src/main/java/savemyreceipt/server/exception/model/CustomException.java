package savemyreceipt.server.exception.model;

import lombok.Getter;
import savemyreceipt.server.exception.ErrorStatus;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public Integer getHttpStatus() {
        return errorStatus.getHttpStatusCode();
    }
}

