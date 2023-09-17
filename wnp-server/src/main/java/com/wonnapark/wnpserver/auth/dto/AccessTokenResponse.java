package com.wonnapark.wnpserver.auth.dto;

public record AccessTokenResponse(
        String grantType,
        String accessToken,
        Long accessTokenExpiresIn
) {
}
