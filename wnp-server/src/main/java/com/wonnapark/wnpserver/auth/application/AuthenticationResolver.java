package com.wonnapark.wnpserver.auth.application;

import com.wonnapark.wnpserver.auth.RefreshToken;
import com.wonnapark.wnpserver.auth.config.JwtProperties;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.auth.infrastructure.RefreshTokenRepository;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import com.wonnapark.wnpserver.user.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class AuthenticationResolver {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Key key;

    public AuthenticationResolver(RefreshTokenRepository refreshTokenRepository, RedisTemplate redisTemplate, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public void validateAccessToken(String accessToken) {
        if (isValidToken(accessToken)) {
            String expiredAccessToken = redisTemplate.opsForValue().get(accessToken);

            if (!Strings.hasText(expiredAccessToken))
                return;

            if (!expiredAccessToken.equals(TokenConstants.LOGOUT))
                return;
        }
        throw new JwtInvalidException(ErrorCode.LOGOUT_TOKEN);
    }

    // 레디스로 만료기간을 같이 관리하니까 정합성이 안 맞을 수 있다. -> 순간의 차이로 정합성이 안맞아도 곧 만료이기 때문에 재로그인으로 이어지는 흐름은 같다.
    public void validateRefreshToken(String token) {
        if (isValidToken(token)) {
            Claims claims = parseClaims(token);
            Long userId = Long.valueOf(claims.getSubject());
            RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                    .orElseThrow(() -> new JwtInvalidException(ErrorCode.EXPIRED_REFRESH_TOKEN));
            if (refreshToken.getValue().equals(token))
                return;
        }
        throw new JwtInvalidException(ErrorCode.EXPIRED_REFRESH_TOKEN);
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtInvalidException(ErrorCode.EXPIRED_ACCESS_TOKEN, expiredJwtException);
        } catch (SignatureException signatureException) {
            throw new JwtInvalidException(ErrorCode.WRONG_SIGNATURE_TOKEN, signatureException);
        } catch (MalformedJwtException | UnsupportedJwtException unsupportedJwtException) {
            throw new JwtInvalidException(ErrorCode.UNSUPPORTED_TOKEN, unsupportedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new JwtInvalidException(ErrorCode.TOKEN_NOT_FOUND, illegalArgumentException);
        }
    }

    public Authentication extractAuthentication(String token) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        int age = (int) claims.get(TokenConstants.AGE_CLAIM_NAME);
        String roleString = String.valueOf(claims.get(TokenConstants.ROLE_CLAIM_NAME));
        Role userRole = Role.valueOf(roleString);
        return Authentication.of(subject, age, userRole);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
