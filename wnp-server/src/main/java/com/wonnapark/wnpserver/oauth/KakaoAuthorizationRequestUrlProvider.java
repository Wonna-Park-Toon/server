package com.wonnapark.wnpserver.oauth;

import com.wonnapark.wnpserver.oauth.config.OauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthorizationRequestUrlProvider implements AuthorizationRequestUrlProvider {

    private static final String RESPONSE_TYPE = "code";

    private final OauthProperties oauthProperties;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String provideUrl() {
        return UriComponentsBuilder
                .fromUriString(oauthProperties.getKakao().getUrl().getAuthorizeUrl())
                .queryParam("client_id", oauthProperties.getKakao().getClientId())
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("redirect_uri", oauthProperties.getKakao().getUrl().getRedirectUrl())
                .toUriString();
    }

}
