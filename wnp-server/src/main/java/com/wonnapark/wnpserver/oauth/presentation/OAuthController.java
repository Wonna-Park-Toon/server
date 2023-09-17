package com.wonnapark.wnpserver.oauth.presentation;

import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.application.OAuthLoginService;
import com.wonnapark.wnpserver.oauth.dto.request.KakaoLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.request.NaverLoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthLoginService oauthLoginService;

    @GetMapping("/{oAuthProvider}")
    @ResponseStatus(HttpStatus.FOUND)
    public void redirectAuthorizationRequestUrl(@PathVariable OAuthProvider oAuthProvider, HttpServletResponse response) throws IOException {
        String url = oauthLoginService.getAuthorizationRequestUrl(oAuthProvider);
        response.sendRedirect(url);
    }

    @GetMapping("login/kakao")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthTokenResponse> loginWithKakao(@ModelAttribute KakaoLoginRequest request) {
        return ApiResponse.from(oauthLoginService.login(request));
    }

    @GetMapping("login/naver")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthTokenResponse> loginWithNaver(@ModelAttribute NaverLoginRequest request) {
        return ApiResponse.from(oauthLoginService.login(request));
    }

}
