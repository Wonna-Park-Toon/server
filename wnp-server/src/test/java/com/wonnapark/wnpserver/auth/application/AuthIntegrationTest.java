package com.wonnapark.wnpserver.auth.application;

import com.wonnapark.wnpserver.auth.config.JwtProperties;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.dto.AccessTokenResponse;
import com.wonnapark.wnpserver.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.config.TestContainerConfig;
import com.wonnapark.wnpserver.global.auth.Authentication;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(TestContainerConfig.class)
@SpringBootTest
class AuthIntegrationTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationResolver authenticationResolver;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("AccessToken 생성 성공 테스트")
    void generateAccessTokenTest() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);

        // when
        AccessTokenResponse accessTokenResponse = jwtTokenService.generateAccessToken(authTokenRequest);

        // then
        assertThat(accessTokenResponse.grantType()).isEqualTo(TokenConstants.BEARER_TYPE);
        assertThat(accessTokenResponse.accessTokenExpiresIn()).isEqualTo(jwtProperties.accessTokenExpireTime());
    }

    @Test
    @DisplayName("RefreshToken 생성 성공 테스트")
    void generateRefreshTokenTest() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);

        // when
        RefreshTokenResponse refreshTokenResponse = jwtTokenService.generateRefreshToken(authTokenRequest);

        // then
        assertThat(refreshTokenResponse.grantType()).isEqualTo(TokenConstants.BEARER_TYPE);
    }

    @Test
    @DisplayName("AuthToken 생성 성공 테스트")
    void generateAuthTokenTest() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);

        // when
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(authTokenRequest);

        // then
        assertThat(authTokenResponse.grantType()).isEqualTo(TokenConstants.BEARER_TYPE);
        assertThat(authTokenResponse.accessTokenExpiresIn()).isEqualTo(jwtProperties.accessTokenExpireTime());
    }

    @Test
    @DisplayName("올바른 AccessToken 검증 테스트")
    void validateRightAccessToken() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);
        AccessTokenResponse accessTokenResponse = jwtTokenService.generateAccessToken(authTokenRequest);

        // when
        Authentication authentication = authenticationResolver.extractAuthentication(accessTokenResponse.accessToken());

        // then
        assertThatNoException().isThrownBy(() -> authenticationResolver.validateAccessToken(accessTokenResponse.accessToken()));
        assertThat(authTokenRequest.userId()).isEqualTo(authentication.userId());
        assertThat(authTokenRequest.age()).isEqualTo(authentication.age());
        assertThat(authTokenRequest.role()).isEqualTo(authentication.role());
    }

    @Test
    @DisplayName("잘못된 AccessToken 검증 테스트")
    void validateWrongAccessToken() {
        assertThatThrownBy(() -> authenticationResolver.validateAccessToken(Instancio.create(String.class)))
                .isInstanceOf(JwtInvalidException.class);
    }

    @Test
    @DisplayName("올바른 RefreshToken 검증 테스트")
    void validateRightRefreshToken() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(authTokenRequest);
        String accessToken = authTokenResponse.accessToken();
        String refreshToken = authTokenResponse.refreshToken();

        // when
        Authentication authentication = authenticationResolver.extractAuthentication(accessToken);

        // then
        assertThatNoException().isThrownBy(() -> authenticationResolver.validateRefreshToken(refreshToken));
    }

    @Nested
    @DisplayName("잘못된 RefreshToken을 검증")
    class ValidateRefreshToken {

        @Test
        @DisplayName("RefreshToken 자체가 잘못된 경우 에러를 반환")
        void validateWrongRefreshToken() {
            assertThatThrownBy(() ->
                    authenticationResolver.validateRefreshToken(Instancio.create(String.class)))
                    .isInstanceOf(JwtInvalidException.class);
        }

        @Test
        @DisplayName("RefreshToken은 유효하지만 저장소와 일치하지 않는 경우 false를 반환")
        void validateNotEqualRefreshToken() {
            // given
            AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);
//            Long userId = authTokenRequest.userId();
            RefreshTokenResponse refreshTokenResponse = jwtTokenService.generateRefreshToken(authTokenRequest);
            String wrongToken = refreshTokenResponse.refreshToken();

            // when
            jwtTokenService.generateRefreshToken(authTokenRequest);

            // then
            assertThatThrownBy(() -> authenticationResolver.validateRefreshToken(wrongToken))
                    .isInstanceOf(JwtInvalidException.class);
        }
    }

}
