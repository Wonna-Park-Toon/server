package com.wonnapark.wnpserver.auth.dto;

import com.wonnapark.wnpserver.auth.RefreshToken;
import com.wonnapark.wnpserver.auth.config.TokenConstants;

public record RefreshTokenResponse(
        String grantType,
        String refreshToken
) {
    public static RefreshTokenResponse from(RefreshToken refreshToken) {
        return new RefreshTokenResponse(TokenConstants.BEARER_TYPE, refreshToken.getValue());
    }
}
