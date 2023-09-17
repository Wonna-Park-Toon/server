package com.wonnapark.wnpserver.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorMessage {

    USER_NOT_FOUND("해당 플랫폼(%s)의 유저(Id : %s)를 찾을 수 없습니다");

    private final String message;

    public String getMessage(Object... wrongCondition) {
        return String.format(message, wrongCondition);
    }
}
