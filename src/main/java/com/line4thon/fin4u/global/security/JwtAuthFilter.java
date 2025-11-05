package com.line4thon.fin4u.global.security;

import com.line4thon.fin4u.global.web.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String token = cookieUtil.getAccessToken(req);
        if (StringUtils.hasText(token) && jwtProvider.isValid(token)) {
            String email = jwtProvider.getSubject(token);

            // 간단한 Authentication (DB hit 없이 이메일만 인증 주체로 사용)
            var auth = new UsernamePasswordAuthenticationToken(email, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(req, res);
    }
}
