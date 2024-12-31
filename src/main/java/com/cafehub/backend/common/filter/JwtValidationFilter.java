package com.cafehub.backend.common.filter;

import com.cafehub.backend.domain.member.login.exception.AuthorizationHeaderNotExistException;
import com.cafehub.backend.domain.member.login.exception.InvalidAuthorizationTokenTypeException;
import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.cafehub.backend.common.constants.CafeHubConstants.AUTHORIZATION_HEADER;
import static com.cafehub.backend.common.constants.CafeHubConstants.BEARER_TOKEN_TYPE;

@Slf4j
@RequiredArgsConstructor
public class JwtValidationFilter implements Filter {

    private final JwtValidator jwtValidator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JwtValidationFilter Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("JWT가 필요한 Request 발생");

        // Request 헤더에서 "토큰타입 + 토큰" 추출
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        // JWT가 필요한 요청인데 Authorization Header에 아무 값이 존재하지 않는 경우 예외 처리
        if(authorizationHeader == null){
            
            // 비 로그인 회원의 optional-auth 요청으로 식별
            if(httpServletRequest.getRequestURI().startsWith("/api/optional-auth")) {
                chain.doFilter(request, response);
                return;
            }
            throw new AuthorizationHeaderNotExistException();
        }

        // Authorization Header가 존재하는데 토큰 타입이 Bearer가 아닌 경우 예외 처리
        if(!authorizationHeader.startsWith(BEARER_TOKEN_TYPE)) throw new InvalidAuthorizationTokenTypeException();

        // Authorization Header에서 실제 토큰 추출
        String accessToken = authorizationHeader.substring(7);

        // 추출한 jwt AccessToken JWT Validator로 검증
        jwtValidator.validateJwtAccessToken(accessToken);

        // 다음 필터로 넘어감
        httpServletRequest.setAttribute("JwtAccessToken", accessToken);
        chain.doFilter(request,response);
    }
}
