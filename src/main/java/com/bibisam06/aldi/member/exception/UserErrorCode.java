package com.bibisam06.aldi.member.exception;

import com.bibisam06.aldi.common.jwt.exception.base.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(404, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    USERID_NOT_FOUND(404, "USERID_NOT_FOUND", "해당 아이디를 가지는 사용자가 존재하지 않습니다."),
    EMAIL_ALREADY_EXISTS(400, "EMAIL_DUPLICATED", "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(401, "WRONG_PASSWORD", "비밀번호가 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(401, "EXPIRED_REFRESH_TOKEN", "리프레쉬 토큰이 만료되었습니다.");



    private final int httpStatus;
    private final String code;
    private final String message;


}

