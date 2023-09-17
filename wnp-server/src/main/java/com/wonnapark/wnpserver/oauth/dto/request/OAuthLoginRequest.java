package com.wonnapark.wnpserver.oauth.dto.request;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginRequest {

    OAuthProvider getOAuthProvider();

    MultiValueMap<String, String> makeBody();

}
