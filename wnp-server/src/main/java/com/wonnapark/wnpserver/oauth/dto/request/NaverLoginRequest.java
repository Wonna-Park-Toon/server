package com.wonnapark.wnpserver.oauth.dto.request;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class NaverLoginRequest implements OAuthLoginRequest {

    private final String code;
    private final String state;

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("state", state);
        return body;
    }
}
