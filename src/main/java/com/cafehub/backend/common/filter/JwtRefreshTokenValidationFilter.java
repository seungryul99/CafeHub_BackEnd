package com.cafehub.backend.common.filter;

import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenNotExistException;
import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtRefreshTokenValidationFilter implements Filter {

    private final JwtValidator jwtValidator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JwtRefreshTokenValidationFilter Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) throw new JwtRefreshTokenNotExistException();

        String jwtRefreshToken = null;

        for(Cookie cookie : cookies){

            if(cookie.getName().equals("JwtRefreshToken")) jwtRefreshToken = cookie.getValue();
        }

        if (jwtRefreshToken == null) throw new JwtRefreshTokenNotExistException();

        // 전달 받은 jwt Refresh Token 검증
        jwtValidator.validateJwtRefreshToken(jwtRefreshToken);

        // 다음 필터로 넘어감
        httpServletRequest.setAttribute("token", jwtRefreshToken);
        chain.doFilter(request,response);

    }
}
