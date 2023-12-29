package savemyreceipt.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    /**
     * 400 BAD_REQUEST
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    WRONG_LOGIN_INFO(HttpStatus.BAD_REQUEST, "WRONG_LOGIN_INFO"),
    ALREADY_EXISTING_EMAIL(HttpStatus.BAD_REQUEST, "ALREADY_EXISTING_EMAIL"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "EXPIRED_TOKEN"),
    ALREADY_IN_GROUP(HttpStatus.BAD_REQUEST, "ALREADY_IN_GROUP"),
    NO_IMAGE(HttpStatus.BAD_REQUEST, "NO_IMAGE"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE"),

    /**
     * 401 UNAUTHORIZED
     */

    /**
     * 403 FORBIDDEN
     */
    NOT_ADMIN(HttpStatus.FORBIDDEN, "NOT_ADMIN"),

    /**
     * 404 NOT_FOUND
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_NOT_FOUND"),

    /**
     * 500 INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
