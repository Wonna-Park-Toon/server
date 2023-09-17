package com.wonnapark.wnpserver.oauth.application;

import com.wonnapark.wnpserver.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.user.application.UserService;
import com.wonnapark.wnpserver.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final OAuthRequestService oAuthRequestService;
    private final AuthorizationRequestService authorizationRequestService;

    public String getAuthorizationRequestUrl(OAuthProvider oAuthProvider) {
        return authorizationRequestService.provide(oAuthProvider);
    }

    public AuthTokenResponse login(OAuthLoginRequest request) {
        OAuthInfoResponse response = oAuthRequestService.requestInfo(request);
        AuthTokenRequest authTokenRequest = findOrCreateUser(response);
        return jwtTokenService.generateAuthToken(authTokenRequest);
    }

    private AuthTokenRequest findOrCreateUser(OAuthInfoResponse response) {
        try {
            UserResponse storedUser = userService.findUserByProviderIdAndPlatform(response.getProviderId(), response.getOAuthProvider());
            return new AuthTokenRequest(storedUser.id(), storedUser.age(), storedUser.role());
        } catch (Exception e) {
            UserResponse createdUser = userService.create(response);
            return new AuthTokenRequest(createdUser.id(), createdUser.age(), createdUser.role());
        }
    }

}
