package com.wonnapark.wnpserver.oauth.dto.request;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class KakaoLoginRequest implements OAuthLoginRequest {

    private final String code;

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        System.out.println("authroizationCode = " + code);
        return body;
    }
}
