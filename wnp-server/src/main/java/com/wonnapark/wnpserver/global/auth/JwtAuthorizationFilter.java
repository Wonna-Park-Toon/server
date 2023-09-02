package com.wonnapark.wnpserver.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.domain.auth.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import com.wonnapark.wnpserver.global.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthenticationResolver authenticationResolver;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] blackList = {
                "/api/v1/auth/kakao",
                "/h2-console"
        };
        String path = request.getRequestURI();
        return Arrays.stream(blackList).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String accessToken = extractTokenFromHeader(request, HttpHeaders.AUTHORIZATION);
        try {
            if (authenticationResolver.isValidToken(accessToken)) {
                Authentication authentication = authenticationResolver.extractAuthentication(accessToken);
                AuthenticationContextHolder.setAuthenticationHolder(authentication);
                if (path.equals("/api/v1/auth/reissue")) {
                    String refreshToken = extractTokenFromHeader(request, TokenConstants.REFRESH_TOKEN);
                    if (!validateRefreshToken(refreshToken, authentication.userId())) {
                        setErrorResponse(response, ErrorCode.BAD_REQUEST);
                        log.warn("토큰 예외 정보 : {}", response);
                        AuthenticationContextHolder.clearContext();
                        return;
                    }
                }
                filterChain.doFilter(request, response);
                AuthenticationContextHolder.clearContext();
            }
        } catch (JwtInvalidException jwtInvalidException) {
            setErrorResponse(response, jwtInvalidException.getErrorCode());
            log.warn("토큰 예외 정보 : {}", response);
        }
    }

    public String extractTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

    private boolean validateRefreshToken(String refreshToken, Long userId) {
        if (authenticationResolver.isValidToken(refreshToken)) {
            RefreshTokenResponse refreshTokenResponse = authenticationResolver.findRefreshTokenByUserId(userId);
            if (refreshTokenResponse.refreshToken().equals(refreshToken))
                return true;
        }
        return false;
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getValue());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.create(errorCode);
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}