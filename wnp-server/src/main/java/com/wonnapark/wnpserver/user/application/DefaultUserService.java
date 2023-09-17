package com.wonnapark.wnpserver.user.application;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.user.Role;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.user.dto.UserResponse;
import com.wonnapark.wnpserver.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wonnapark.wnpserver.user.UserErrorMessage.USER_NOT_FOUND;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(OAuthInfoResponse response) {
        User user = User.builder()
                .providerId(response.getProviderId())
                .platform(response.getOAuthProvider())
                .nickname(response.getNickname())
                .age(response.getAge())
                .gender(response.getGender())
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    public UserResponse findUserByProviderIdAndPlatform(String providerId, OAuthProvider platform) {
        User user = userRepository.findByProviderIdAndPlatform(providerId, platform)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage(platform, providerId)));
        return UserResponse.from(user);
    }

}
