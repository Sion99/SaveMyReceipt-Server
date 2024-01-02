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
    WRONG_LOGIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 로그인 정보입니다."),
    ALREADY_EXISTING_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    ALREADY_IN_GROUP(HttpStatus.BAD_REQUEST, "이미 그룹에 속해있습니다."),
    NO_IMAGE(HttpStatus.BAD_REQUEST, "이미지가 없습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "올바르지 않은 파일 형식입니다."),
    NOT_RECEIPT(HttpStatus.BAD_REQUEST, "업로드한 파일에서 영수증을 인식하지 못했습니다."),

    /**
     *  GOOGLE_API_ERROR
     */
    GOOGLE_VISION_API_DUPLICATE_KEY_ERROR(HttpStatus.BAD_REQUEST, "GOOGLE_VISION_API_DUPLICATE_KEY_ERROR"),
    GOOGLE_VISION_API_BAD_IMAGE_DATA(HttpStatus.BAD_REQUEST, "잘못된 이미지 형식입니다."),
    GOOGLE_VISION_API_NOT_RECEIPT(HttpStatus.BAD_REQUEST, "업로드한 파일에서 영수증을 인식하지 못했습니다."),

    /**
     * 401 UNAUTHORIZED
     */
    RECEIPT_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "영수증 권한이 없습니다."),

    /**
     * 403 FORBIDDEN
     */
    NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자가 아닙니다."),

    /**
     * 404 NOT_FOUND
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹이 존재하지 않습니다."),
    RECEIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "영수증이 존재하지 않습니다."),

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
