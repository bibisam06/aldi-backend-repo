package com.bibisam06.aldi.common.jwt.exception.base;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseException extends RuntimeException{
    private final BaseErrorCode errorCode;
}
