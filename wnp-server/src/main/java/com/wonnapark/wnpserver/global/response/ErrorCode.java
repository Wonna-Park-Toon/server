package com.wonnapark.wnpserver.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    BAD_REQUEST(400, "C000", "잘못된 요청입니다."),
    ENTITY_NOT_FOUND(404, "C001", "해당 엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C999", "서버 내부 에러입니다."),

    // Oauth
    OAUTH_RESPONSE_NOT_FOUND(404, "O001", "요청에 해당하는 응답을 찾을 수 없습니다."),

    // Token
    TOKEN_NOT_FOUND(404, "T001", "토큰을 찾을 수 없습니다."),
    EXPIRED_ACCESS_TOKEN(400, "T002", "만료된 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(400, "T002", "토큰이 만료되었습니다. 다시 로그인을 진행해 주세요."),
    UNSUPPORTED_TOKEN(400, "T003", "잘못된 형식의 토큰입니다."),
    WRONG_SIGNATURE_TOKEN(400, "T004", "변조된 토큰입니다. 토큰이 탈취되었을 수도 있습니다."),
    LOGOUT_TOKEN(400, "T005", "로그아웃 처리된 토큰입니다. 로그인을 재시도 해주세요.");

    private final int value;
    private final String code;
    private final String message;

    ErrorCode(int value, String code, String message) {
        this.value = value;
        this.code = code;
        this.message = message;
    }

}
