package com.wonnapark.wnpserver.oauth.infrastructure.client;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.response.OAuthInfoResponse;

public interface OAuthApiClient {

    OAuthProvider getOAuthProvider();

    String requestAccessToken(OAuthLoginRequest request);

    OAuthInfoResponse requestOauthInfo(String accessToken);

}
