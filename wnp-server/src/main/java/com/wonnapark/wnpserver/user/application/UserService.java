package com.wonnapark.wnpserver.user.application;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.user.dto.UserResponse;

public interface UserService {

    UserResponse create(OAuthInfoResponse response);

    UserResponse findUserByProviderIdAndPlatform(String providerId, OAuthProvider platform);
}
