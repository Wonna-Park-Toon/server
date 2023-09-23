package com.wonnapark.wnpserver.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.exception.JwtInvalidException;
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

    private final static String LOGOUT_URI = "/api/v1/auth/logout";
    private final static String REISSUE_URI = "/api/v1/auth/reissue";
    private final static String RE_LOGIN_URI = "/front/login";

    private final AuthenticationResolver authenticationResolver;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String[] blackList = {
                "/api/v1/guest/",
                "/api/v1/oauth/",
                "/h2-console",
                "/swagger-ui/",
                "/v3/api-docs",
                "/wonna-park.json"
        };
        String path = request.getRequestURI();
        return Arrays.stream(blackList).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String accessToken = extractTokenFromHeader(request, HttpHeaders.AUTHORIZATION);
        try {
            authenticationResolver.validateAccessToken(accessToken);
            Authentication authentication = authenticationResolver.extractAuthentication(accessToken);
            AuthenticationContextHolder.setAuthenticationHolder(authentication);

            if (path.equals(REISSUE_URI) || path.equals(LOGOUT_URI)) {
                String refreshToken = extractTokenFromHeader(request, TokenConstants.REFRESH_TOKEN);
                authenticationResolver.validateRefreshToken(refreshToken, authentication.userId());
            }

            filterChain.doFilter(request, response);
        } catch (
                JwtInvalidException jwtInvalidException) {
            setErrorResponse(response, jwtInvalidException);
        } finally {
            AuthenticationContextHolder.clearContext();
        }

    }

    private String extractTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token) && token.startsWith(TokenConstants.BEARER_TYPE)) {
            return token.substring(TokenConstants.BEARER_TYPE.length());
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, JwtInvalidException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        if (errorCode.equals(ErrorCode.EXPIRED_ACCESS_TOKEN)) response.setHeader(HttpHeaders.LOCATION, REISSUE_URI);
        if (errorCode.equals(ErrorCode.EXPIRED_REFRESH_TOKEN)) response.setHeader(HttpHeaders.LOCATION, RE_LOGIN_URI);
        response.setStatus(errorCode.getValue());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.create(errorCode);
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
            log.warn("토큰 예외 정보 : {}", errorResponse, exception);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

}
