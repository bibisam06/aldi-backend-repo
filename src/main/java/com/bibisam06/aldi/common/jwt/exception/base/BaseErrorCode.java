package com.bibisam06.aldi.common.jwt.exception.base;

public interface BaseErrorCode {
    public String getCode();
    public String getMessage();
    public int getHttpStatus();
}
