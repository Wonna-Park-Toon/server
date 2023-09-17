package com.wonnapark.wnpserver.oauth.infrastructure.client;

import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.config.OauthProperties;
import com.wonnapark.wnpserver.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.response.NaverInfoResponse;
import com.wonnapark.wnpserver.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.oauth.infrastructure.NaverToken;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String EMPTY_SPACE = " ";

    private final OauthProperties oauthProperties;
    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public String requestAccessToken(OAuthLoginRequest param) {
        String url = oauthProperties.getNaver().getUrl().getRequestTokenUrl();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = param.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", oauthProperties.getNaver().getClientId());
        body.add("client_secret", oauthProperties.getNaver().getClientSecret());

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        NaverToken response = restTemplate.postForObject(url, request, NaverToken.class);
        Assert.notNull(response, ErrorCode.OAUTH_RESPONSE_NOT_FOUND.getMessage());
        return response.accessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = oauthProperties.getNaver().getUrl().getRequestInfoUrl();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, TokenConstants.BEARER_TYPE + EMPTY_SPACE + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        NaverInfoResponse naverInfoResponse = restTemplate.postForObject(url, request, NaverInfoResponse.class);
        Assert.notNull(naverInfoResponse, ErrorCode.OAUTH_RESPONSE_NOT_FOUND.getMessage());
        return naverInfoResponse;
    }
}
