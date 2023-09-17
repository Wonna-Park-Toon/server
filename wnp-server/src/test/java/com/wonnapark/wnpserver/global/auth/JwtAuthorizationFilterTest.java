package com.wonnapark.wnpserver.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;

@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterTest {

    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Mock
    AuthenticationResolver authenticationResolver;

    @BeforeEach
    void setUp() {
        jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationResolver, new ObjectMapper());
    }

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private MockHttpServletResponse response = new MockHttpServletResponse();

    private FilterChain filterChain = Mockito.mock(FilterChain.class);

    @ParameterizedTest
    @ValueSource(strings =
            {"/api/v1/guest/",
                    "/api/v1/oauth/",
                    "/h2-console"})
    @DisplayName("필터 적용 요청이 아니라면 필터를 적용하지 않는다.")
    void test(String requestURI) throws ServletException {
        // given
        request.setRequestURI(requestURI);

        // when
        jwtAuthorizationFilter.shouldNotFilter(request);

        // then
        then(filterChain).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("필터 적용이 필요한 요청이라면 액세스 토큰을 검증한다.")
    void validateAccessTokenWhenNecessaryTest() throws ServletException, IOException {
        // given
        String accessToken = "accessToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        Authentication authentication = AuthFixtures.createUserAuthentication();

        willDoNothing().given(authenticationResolver).validateAccessToken(any());
        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);

        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // then
        then(authenticationResolver).should(atLeastOnce()).validateAccessToken(any());
        then(authenticationResolver).should(atLeastOnce()).extractAuthentication(any());
        then(filterChain).should(atLeastOnce()).doFilter(request, response);
    }

    @Test
    @DisplayName("액세스 토큰이 올바르지 않다면 에러를 지정하고 반환한다")
    void setErrorResponseWhenAccessTokenIsWrong() throws ServletException, IOException {
        // given
        String accessToken = "wrongToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        ErrorCode errorCode = ErrorCode.UNSUPPORTED_TOKEN;
        int status = errorCode.getValue();

        willThrow(new JwtInvalidException(errorCode)).given(authenticationResolver).validateAccessToken(any());

        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // then
        then(authenticationResolver).should(atLeastOnce()).validateAccessToken(any());
        then(filterChain).shouldHaveNoInteractions();
        assertThat(status).isEqualTo(response.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings =
            {"/api/v1/auth/reissue",
                    "/api/v1/auth/logout"})
    @DisplayName("재발급, 로그아웃 요청시 액세스 토큰과 리프레시 토큰 모두 검증한다.")
    void validateAccessAndRefreshTokenWhenRequestIsReissueOrLogout(String requestURI) throws ServletException, IOException {
        // given
        request.setRequestURI(requestURI);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        request.addHeader(TokenConstants.REFRESH_TOKEN, refreshToken);
        Authentication authentication = AuthFixtures.createUserAuthentication();

        willDoNothing().given(authenticationResolver).validateAccessToken(any());
        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);
        willDoNothing().given(authenticationResolver).validateRefreshToken(any(), any());

        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // then
        then(authenticationResolver).should(atLeastOnce()).validateAccessToken(any());
        then(authenticationResolver).should(atLeastOnce()).validateRefreshToken(any(), any());
        then(filterChain).should(atMostOnce()).doFilter(request, response);
    }

    @ParameterizedTest
    @ValueSource(strings =
            {"/api/v1/auth/reissue",
                    "/api/v1/auth/logout"})
    @DisplayName("재발급, 로그아웃 요청시 리프레시 토큰이 올바르지 않다면 에러를 지정하고 반환한다.")
    void setErrorResponseWhenRefreshTokenIsWrong(String requestURI) throws ServletException, IOException {
        // given
        request.setRequestURI(requestURI);
        String accessToken = "accessToken";
        String refreshToken = "wrongToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        request.addHeader(TokenConstants.REFRESH_TOKEN, refreshToken);
        ErrorCode errorCode = ErrorCode.UNSUPPORTED_TOKEN;
        int status = errorCode.getValue();
        Authentication authentication = AuthFixtures.createUserAuthentication();

        willDoNothing().given(authenticationResolver).validateAccessToken(any());
        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);
        willThrow(new JwtInvalidException(errorCode)).given(authenticationResolver).validateRefreshToken(any(), any());

        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // then
        then(authenticationResolver).should(atLeastOnce()).validateAccessToken(any());
        then(authenticationResolver).should(atLeastOnce()).validateRefreshToken(any(), any());
        then(filterChain).shouldHaveNoInteractions();
        assertThat(status).isEqualTo(response.getStatus());
    }

}
