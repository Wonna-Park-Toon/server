package com.wonnapark.wnpserver.oauth.application;

import com.wonnapark.wnpserver.oauth.AuthorizationRequestUrlProvider;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuthorizationRequestService {

    private final Map<OAuthProvider, AuthorizationRequestUrlProvider> providers;

    public AuthorizationRequestService(Set<AuthorizationRequestUrlProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toUnmodifiableMap(AuthorizationRequestUrlProvider::oAuthProvider, Function.identity()));
    }

    public String provide(OAuthProvider oAuthProvider) {
        AuthorizationRequestUrlProvider authorizationRequestUrlProvider = providers.get(oAuthProvider);
        return authorizationRequestUrlProvider.provideUrl();
    }

}
