package com.wonnapark.wnpserver.oauth.application;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.oauth.infrastructure.client.OAuthApiClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthRequestService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public OAuthRequestService(Set<OAuthApiClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toUnmodifiableMap(OAuthApiClient::getOAuthProvider, Function.identity()));
    }

    public OAuthInfoResponse requestInfo(OAuthLoginRequest request) {
        OAuthApiClient oAuthApiClient = clients.get(request.getOAuthProvider());
        String accessToken = oAuthApiClient.requestAccessToken(request);
        return oAuthApiClient.requestOauthInfo(accessToken);
    }

}
