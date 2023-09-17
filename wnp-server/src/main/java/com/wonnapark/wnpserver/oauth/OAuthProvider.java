package com.wonnapark.wnpserver.oauth;

import java.util.Arrays;

public enum OAuthProvider {
    KAKAO,
    NAVER;

    public static OAuthProvider from(String platform) {
        return Arrays.stream(OAuthProvider.values())
                .filter(v -> v.name().equals(platform.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s와 일치하는 플랫폼이 없습니다.", platform)));
    }

}
