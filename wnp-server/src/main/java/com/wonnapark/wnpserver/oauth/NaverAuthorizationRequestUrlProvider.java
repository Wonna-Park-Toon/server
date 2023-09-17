package com.wonnapark.wnpserver.oauth;

import com.wonnapark.wnpserver.oauth.config.OauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverAuthorizationRequestUrlProvider implements AuthorizationRequestUrlProvider {

    private static final String RESPONSE_TYPE = "code";

    private final OauthProperties oauthProperties;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public String provideUrl() {
        return UriComponentsBuilder
                .fromUriString(oauthProperties.getNaver().getUrl().getAuthorizeUrl())
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("client_id", oauthProperties.getNaver().getClientId())
                .queryParam("state", oauthProperties.getNaver().getState())
                .queryParam("redirect_uri", oauthProperties.getNaver().getUrl().getRedirectUrl())
                .toUriString();
    }

}
