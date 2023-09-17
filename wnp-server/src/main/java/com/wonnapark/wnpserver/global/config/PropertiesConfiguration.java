package com.wonnapark.wnpserver.global.config;

import com.wonnapark.wnpserver.auth.config.JwtProperties;
import com.wonnapark.wnpserver.oauth.config.OauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {OauthProperties.class, JwtProperties.class})
public class PropertiesConfiguration {
}
