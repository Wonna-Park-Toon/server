package com.wonnapark.wnpserver.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secretKey,
        long accessTokenExpireTime,
        long refreshTokenExpireTime
) {
}
