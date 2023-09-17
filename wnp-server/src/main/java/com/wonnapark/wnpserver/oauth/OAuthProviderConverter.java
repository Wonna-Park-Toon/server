package com.wonnapark.wnpserver.oauth;

import jakarta.validation.constraints.NotBlank;
import org.springframework.core.convert.converter.Converter;

public class OAuthProviderConverter implements Converter<String, OAuthProvider> {

    @Override
    public OAuthProvider convert(@NotBlank String platform) {
        return OAuthProvider.from(platform);
    }
    
}
