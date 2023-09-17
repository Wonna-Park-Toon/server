package com.wonnapark.wnpserver.auth.application;

import com.wonnapark.wnpserver.auth.RefreshToken;
import com.wonnapark.wnpserver.auth.config.JwtProperties;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.dto.AccessTokenResponse;
import com.wonnapark.wnpserver.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.auth.infrastructure.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@Component
public class JwtTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtTokenService(RefreshTokenRepository refreshTokenRepository, RedisTemplate<String, String> redisTemplate, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessTokenResponse generateAccessToken(AuthTokenRequest request) {
        long now = new Date().getTime();
        Date expiredAt = new Date(now + jwtProperties.refreshTokenExpireTime());

        String accessToken = Jwts.builder()
                .claim(TokenConstants.AGE_CLAIM_NAME, request.age())
                .claim(TokenConstants.ROLE_CLAIM_NAME, request.role())
                .setSubject(String.valueOf(request.userId()))
                .setIssuer(TokenConstants.ISSUER)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 제어권이 없다. -> 유틸 클래스 Mock & Clock 고민해보기
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new AccessTokenResponse(TokenConstants.BEARER_TYPE, accessToken, jwtProperties.accessTokenExpireTime());
    }

    public RefreshTokenResponse generateRefreshToken(AuthTokenRequest request) {
        long now = new Date().getTime();
        Date expiredAt = new Date(now + jwtProperties.refreshTokenExpireTime());
        RandomGenerator generator = RandomGenerator.getDefault();
        int identifyNum = generator.nextInt();

        String refreshToken = Jwts.builder()
                .setIssuer(String.format(TokenConstants.ISSUER + "%d", identifyNum))
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(request.userId())
                .value(refreshToken)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return new RefreshTokenResponse(TokenConstants.BEARER_TYPE, refreshToken);
    }

    public AuthTokenResponse generateAuthToken(AuthTokenRequest request) {
        AccessTokenResponse accessTokenResponse = generateAccessToken(request);
        RefreshTokenResponse refreshTokenResponse = generateRefreshToken(request);

        return AuthTokenResponse.builder()
                .grantType(TokenConstants.BEARER_TYPE)
                .accessToken(accessTokenResponse.accessToken())
                .accessTokenExpiresIn(jwtProperties.accessTokenExpireTime())
                .refreshToken(refreshTokenResponse.refreshToken())
                .build();
    }

    public void logoutAccessToken(String accessToken) {
        redisTemplate.opsForValue()
                .set(
                        accessToken,
                        TokenConstants.LOGOUT,
                        getExpirationMilliSeconds(accessToken),
                        TimeUnit.MILLISECONDS
                );
    }

    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }

    private Long getExpirationMilliSeconds(String accessToken) {
        long now = new Date().getTime();
        Date expiration = parseClaims(accessToken).getExpiration();
        return expiration.getTime() - now;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
