package com.wonnapark.wnpserver.auth.dto;

import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.user.Role;

public record AuthTokenRequest(
        Long userId,
        int age,
        Role role
) {
    public static AuthTokenRequest from(Authentication authentication) {
        return new AuthTokenRequest(authentication.userId(), authentication.age(), authentication.role());
    }

}
