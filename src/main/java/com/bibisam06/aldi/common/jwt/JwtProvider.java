package com.bibisam06.aldi.common.jwt;

import com.bibisam06.aldi.common.jwt.dto.AccessTokenDTO;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.member.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    String getIssuer(){
        return jwtProperties.getIssuer();
    }

    SecretKey getSecret(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
    // 토큰 생성 로직
    public JwtToken generateToken(Integer userId, UserRole userRole) {

        Date now = new Date();
        Long accessExpiration = jwtProperties.getAccessTokenExp();
        Long refreshExpiration = jwtProperties.getRefreshTokenExp();

        String accessToken = builder()
                .subject(userId.toString())
                .issuer("bibisam06")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessExpiration ))
                .claim("role", userRole)
                .signWith(getSecret())
                .compact();

        String refreshToken = builder()
                .subject(userId.toString())
                .issuer("bibisam06")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshExpiration))
                .claim("role", userRole)
                .signWith(getSecret())
                .compact();


        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    // jwt 토큰 복호화 후 정보 꺼내는 메서드 
    public AccessTokenDTO getAuthentication(String accessToken) {
        
        Claims claims = parseClaim(accessToken);

        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuer());

        return new AccessTokenDTO(
                claims.getSubject(),
                (String) claims.get("role")
        );

    }

    private Claims parseClaim(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecret())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload(); // Claims
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
