package com.bibisam06.aldi.common.jwt;

import com.bibisam06.aldi.common.jwt.dto.AccessTokenDTO;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.common.jwt.exception.GlobalErrorCode;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public JwtToken generateToken(Integer userId, UserRole userRole) {
        Date now = new Date();
        Long accessExpiration = jwtProperties.getAccessTokenExp();
        Long refreshExpiration = jwtProperties.getRefreshTokenExp();

        String accessToken = Jwts.builder()
                .setSubject(userId.toString())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpiration))
                .claim("role", userRole)
                .signWith(getSecretKey())
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userId.toString())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpiration))
                .claim("role", userRole)
                .signWith(getSecretKey())
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AccessTokenDTO getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return new AccessTokenDTO(
                claims.getSubject(),
                String.valueOf(claims.get("role"))
        );
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new BaseException(GlobalErrorCode.INVALID_TOKEN);
        }
    }

    public long getExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            // 1. 토큰 파싱 (서명 검증 포함)
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            // 2. 블랙리스트 확인 (선택: Redis 사용 시)
            if (redisTemplate.hasKey(token)) {
                return false; // 로그아웃된 토큰
            }

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature or malformed token");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty");
        }

        return false;
    }

}
