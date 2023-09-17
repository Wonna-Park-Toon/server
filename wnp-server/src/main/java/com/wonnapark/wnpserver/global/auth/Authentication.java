package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.user.Role;

public record Authentication(
        Long userId,
        int age,
        Role role
) {
    public static Authentication of(String subject, int age, Role role) {
        return new Authentication(Long.valueOf(subject), age, role);
    }

    public static Authentication initAuthentication() {
        return new Authentication(-1L, -1, Role.GUEST);
    }
}
