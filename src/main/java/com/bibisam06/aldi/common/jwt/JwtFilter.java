package com.bibisam06.aldi.common.jwt;

import com.bibisam06.aldi.common.jwt.dto.AccessTokenDTO;
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

    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        try {
            if (StringUtils.hasText(token) && jwtProvider.getAuthentication(token) != null) {

                // 사용자 정보 추출
                AccessTokenDTO accessTokenDTO = jwtProvider.getAuthentication(token);

                // 권한 부여
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                accessTokenDTO.getUserId(),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + accessTokenDTO.getUserRole()))
                        );

                // SecurityContext에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            log.warn("JWT 처리 중 오류 발생: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
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
