package com.wonnapark.wnpserver.auth.dto;

import lombok.Builder;

@Builder
public record AuthTokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {
}
