package com.bibisam06.aldi.common.jwt;

import com.bibisam06.aldi.common.jwt.JwtProperties;
import com.bibisam06.aldi.common.jwt.dto.AccessTokenDTO;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.common.jwt.exception.GlobalErrorCode;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

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
}
