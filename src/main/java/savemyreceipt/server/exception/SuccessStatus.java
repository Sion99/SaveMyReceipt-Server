package savemyreceipt.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus {

    /**
     * 200 OK
     */
    OK(HttpStatus.OK, "OK"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    SIGNUP_SUCCESS(HttpStatus.OK, "회원가입 성공"),
    GET_USER_DETAIL_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
