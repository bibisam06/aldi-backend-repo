package com.bibisam06.aldi.common.jwt;

import com.bibisam06.aldi.common.jwt.dto.AccessTokenDTO;
import com.bibisam06.aldi.common.redis.BlacklistTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final BlacklistTokenService tokenBlacklistService;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted.");
                return;
            }
//            AccessTokenDTO auth = jwtProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }


    /**
     * 요청 헤더에서 JWT 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader()); // 예: Authorization
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) { // 예: Bearer
            return bearerToken.substring(jwtProperties.getPrefix().length()).trim(); // "Bearer " 이후 토큰 반환
        }
        return null;
    }

}
