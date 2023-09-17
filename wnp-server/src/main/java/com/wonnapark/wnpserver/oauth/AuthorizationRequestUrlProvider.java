package com.wonnapark.wnpserver.oauth;

public interface AuthorizationRequestUrlProvider {

    OAuthProvider oAuthProvider();

    String provideUrl();

}
