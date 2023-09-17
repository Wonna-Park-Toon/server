package com.wonnapark.wnpserver.webtoon.application;

import lombok.Getter;

@Getter
public enum WebtoonExceptionMessage {
    WEBTOON_NOT_FOUND("해당 ID의 웹툰을 찾을 수 없습니다: %d"),
    WEBTOON_AUTHORIZATION_REQUIRED("사용자 인증이 필요한 웹툰입니다.: %d"),
    WEBTOON_AGE_RESTRICTED("해당 ID의 웹툰은 18세 이용가입니다.: %d");
    
    private final String message;

    WebtoonExceptionMessage(String message) {
        this.message = message;
    }
}
