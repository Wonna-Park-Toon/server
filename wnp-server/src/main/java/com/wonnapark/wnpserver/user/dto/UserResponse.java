package com.wonnapark.wnpserver.user.dto;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.user.Role;
import com.wonnapark.wnpserver.user.User;

public record UserResponse(
        Long id,
        String providerId,
        OAuthProvider platform,
        String nickname,
        int age,
        String gender,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getProviderId(),
                user.getPlatform(),
                user.getNickname(),
                user.getAge(),
                user.getGender(),
                user.getRole()
        );
    }
}
