package savemyreceipt.server.exception.model;

import savemyreceipt.server.exception.ErrorStatus;

public class NotFoundException extends CustomException {

    public NotFoundException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }
}
