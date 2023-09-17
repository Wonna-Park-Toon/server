package com.wonnapark.wnpserver.auth.exception;

import com.wonnapark.wnpserver.global.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtInvalidException extends RuntimeException {

    private final ErrorCode errorCode;

    public JwtInvalidException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}
