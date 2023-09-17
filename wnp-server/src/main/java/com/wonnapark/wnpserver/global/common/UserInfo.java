package com.wonnapark.wnpserver.global.common;

import com.wonnapark.wnpserver.global.auth.Authentication;

public record UserInfo(
        Long userId,
        int age
) {
    public static UserInfo from(Authentication authentication) {
        return new UserInfo(authentication.userId(), authentication.age());
    }
    
}
