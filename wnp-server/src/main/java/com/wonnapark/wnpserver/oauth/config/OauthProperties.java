package com.wonnapark.wnpserver.oauth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OauthProperties {

    private final Kakao kakao;
    private final Naver naver;

    @Getter
    @RequiredArgsConstructor
    public static final class Kakao {

        private final String clientId;
        private final Url url;

        @RequiredArgsConstructor
        public static final class Url {

            private static String KAKAO_AUTHORIZE_URI = "/oauth/authorize";
            private static String KAKAO_REQUEST_TOKEN_URI = "/oauth/token";
            private static String KAKAO_REQUEST_INFO_URI = "/v2/user/me";

            private final String authUrl;
            private final String apiUrl;
            private final String redirectUrl;

            public String getAuthorizeUrl() {
                return authUrl + KAKAO_AUTHORIZE_URI;
            }

            public String getRequestTokenUrl() {
                return authUrl + KAKAO_REQUEST_TOKEN_URI;
            }

            public String getRequestInfoUrl() {
                return apiUrl + KAKAO_REQUEST_INFO_URI;
            }

            public String getRedirectUrl() {
                return redirectUrl;
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Naver {

        private final String clientId;
        private final String clientSecret;
        private final String state;
        private final Url url;

        @RequiredArgsConstructor
        public static final class Url {

            private static String NAVER_AUTHORIZE_URI = "/oauth2.0/authorize";
            private static String NAVER_REQUEST_TOKEN_URI = "/oauth2.0/token";
            private static String NAVER_REQUEST_INFO_URI = "/v1/nid/me";

            private final String authUrl;
            private final String apiUrl;
            private final String redirectUrl;

            public String getAuthorizeUrl() {
                return authUrl + NAVER_AUTHORIZE_URI;
            }

            public String getRequestTokenUrl() {
                return authUrl + NAVER_REQUEST_TOKEN_URI;
            }

            public String getRequestInfoUrl() {
                return apiUrl + NAVER_REQUEST_INFO_URI;
            }

            public String getRedirectUrl() {
                return redirectUrl;
            }
        }

    }
}
