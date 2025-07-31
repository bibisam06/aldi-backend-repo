package com.bibisam06.aldi.common.jwt;

import com.bibisam06.aldi.auth.repository.RedisBlacklistTokenDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RedisBlacklistTokenDAO tokenBlacklistRedisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            if (tokenBlacklistRedisDao.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted.");
                return;
            }

            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) {
            return bearerToken.substring(jwtProperties.getPrefix().length()).trim();
        }
        return null;
    }
}
