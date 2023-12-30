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
    REISSUE_SUCCESS(HttpStatus.OK, "토큰 재발급 성공"),
    GET_USER_DETAIL_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공"),
    GET_GROUP_SUCCESS(HttpStatus.OK, "그룹 정보 조회 성공"),
    JOIN_GROUP_SUCCESS(HttpStatus.OK, "그룹 가입 성공"),
    RECEIPT_INFO_GET_SUCCESS(HttpStatus.OK, "영수증 정보 조회 성공"),
    RECEIPT_INFO_UPDATE_SUCCESS(HttpStatus.OK, "영수증 정보 업데이트 성공"),
    GET_GROUP_MEMBER_SUCCESS(HttpStatus.OK, "그룹 멤버 조회 성공"),
    GET_GROUP_RECEIPT_SUCCESS(HttpStatus.OK, "그룹 영수증 조회 성공"),
    GET_USER_RECEIPT_SUCCESS(HttpStatus.OK, "유저 영수증 조회 성공"),

    /**
     * 201 CREATED
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
    CREATE_GROUP_SUCCESS(HttpStatus.CREATED, "그룹 생성 성공"),
    IMAGE_UPLOAD_SUCCESS(HttpStatus.CREATED, "이미지 업로드 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
