package com.wonnapark.wnpserver.oauth.application;

import com.wonnapark.wnpserver.auth.application.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLogoutService {

    private final JwtTokenService jwtTokenService;

    public void logout(Long userId, String accessToken) {
        jwtTokenService.logoutAccessToken(accessToken);
        jwtTokenService.deleteRefreshTokenByUserId(userId);
    }

}
