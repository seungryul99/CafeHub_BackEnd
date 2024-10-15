package com.cafehub.backend.common.filter.jwt;

import com.cafehub.backend.domain.member.jwt.JwtValidator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


@Slf4j
@RequiredArgsConstructor
public class JwtCheckFilter implements Filter {

    private final JwtValidator jwtValidator;
    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private static final String AUTHORIZATION_HEADER_VALUE_TYPE = "Bearer ";
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String OPTIONAL_AUTH_PATH = "/api/optional-auth";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JWT CHECK FILTER 초기화");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("/api/auth/*, /api/optional-auth/* URI의 request JWT Check Filter에 들어옴");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorizationHeaderValue = httpServletRequest.getHeader(AUTHORIZATION_HEADER_KEY);
        String requestUri = httpServletRequest.getRequestURI();



        if(authorizationHeaderValue == null){

            if (requestUri.startsWith(OPTIONAL_AUTH_PATH)){
                log.info("/api/optional-auth/* URI의 request 비로그인 사용자에 대해서 정상적으로 처리 완료됨");
                chain.doFilter(request, response);
                return;
            }
                
            // 반드시 인증이 필요한데 authorizationHeaderValue에 아무것도 존재하지 않는 경우 예외발생
            throw new RuntimeException();
        }


        String jwtAccessToken = extractTokenFromAuthorizationHeader(authorizationHeaderValue);


        if (jwtValidator.validateJwtAccessToken(jwtAccessToken)) {

            try {
                jwtThreadLocalStorage.initJwtAccessTokenHolder(jwtAccessToken);
                chain.doFilter(request, response);
            }
            finally {
                jwtThreadLocalStorage.clearJwtAccessTokenHolder();
                log.info("ThreadLocal Resource 정리");
            }
        }
    }


    private String extractTokenFromAuthorizationHeader(String authorizationHeaderValue){

        if (authorizationHeaderValue.startsWith(AUTHORIZATION_HEADER_VALUE_TYPE)){
            return authorizationHeaderValue.substring(7);
        }
        else {
            // 토큰 타입이 유효 하지 않은 오류 발생
            throw new RuntimeException();
        }
    }
}
