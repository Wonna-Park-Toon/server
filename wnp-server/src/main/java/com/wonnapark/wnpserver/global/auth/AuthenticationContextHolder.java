package com.wonnapark.wnpserver.global.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationContextHolder {

    private static final ThreadLocal<Authentication> authenticationHolder =
            ThreadLocal.withInitial(Authentication::initAuthentication);

    public static void clearContext() {
        authenticationHolder.remove();
    }

    public static Authentication getAuthentication() {
        return authenticationHolder.get();
    }

    public static void setAuthenticationHolder(Authentication authentication) {
        authenticationHolder.set(authentication);
    }

}
