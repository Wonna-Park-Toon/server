package com.wonnapark.wnpserver.oauth.dto.response;

import com.wonnapark.wnpserver.oauth.OAuthProvider;

public interface OAuthInfoResponse {

    String getProviderId();

    String getNickname();

    OAuthProvider getOAuthProvider();

    int getAge();

    String getGender();
}
