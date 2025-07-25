package com.bibisam06.aldi.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-exp}")
    private Long accessTokenExp;

    @Value("${jwt.refresh-token-exp}")
    private Long refreshTokenExp;

    @Value("${jwt.issuer")
    private String issuer;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;
}
