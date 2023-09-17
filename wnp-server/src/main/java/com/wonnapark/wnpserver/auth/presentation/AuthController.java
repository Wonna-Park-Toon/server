package com.wonnapark.wnpserver.auth.presentation;

import com.wonnapark.wnpserver.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.auth.AuthenticationContextHolder;
import com.wonnapark.wnpserver.global.auth.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.oauth.application.OAuthLogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthLogoutService oAuthLogoutService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthTokenResponse> reissueAuthToken() {
        Authentication authentication = AuthenticationContextHolder.getAuthentication();
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(AuthTokenRequest.from(authentication));
        return ApiResponse.from(authTokenResponse);
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Authorized UserInfo userInfo, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(TokenConstants.BEARER_TYPE.length());
        oAuthLogoutService.logout(userInfo.userId(), accessToken);
    }

}
