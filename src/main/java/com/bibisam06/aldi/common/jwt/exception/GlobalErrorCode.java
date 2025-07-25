package com.bibisam06.aldi.common.jwt.exception;

import com.bibisam06.aldi.common.jwt.exception.base.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements BaseErrorCode {

    //✅ 공통 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 금지된 리소스입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // ✅ 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    // ✅ 기타 예시
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    GlobalErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public int getHttpStatus() {
        return 0;
    }
}
