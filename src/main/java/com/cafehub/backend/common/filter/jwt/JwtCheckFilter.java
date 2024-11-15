package com.cafehub.backend.common.filter.jwt;

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
public class JwtCheckFilter implements Filter {

    private static final String OPTIONAL_AUTH_PATH = "/api/optional-auth";
    private final JwtValidator jwtValidator;
    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JWT CHECK FILTER 초기화");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("/api/auth/*, /api/optional-auth/* URI의 request가 JWT Check Filter에 들어옴");

        
        // Request 헤더에서 "토큰타입 + 토큰" 추출, requestURI 추출
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorizationHeaderValue = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        String requestUri = httpServletRequest.getRequestURI();


        // Request 헤더에 "토큰타입 + 토큰"이 존재하지 않는경우 JWTCheckFilter를 거쳐야 하는 요청인데 JWT가 존재하지 않음으로 간주
        if(authorizationHeaderValue == null){


            // /api/optional-check 요청에 대해서는 로그인 하지 않은 사용자가 보낸 요청으로 간주하고 정상적으로 필터 통과
            if (requestUri.startsWith(OPTIONAL_AUTH_PATH)){
                log.info("/api/optional-auth/* URI의 request 비로그인 사용자에 대해서 정상적으로 처리 완료됨");
                chain.doFilter(request, response);
                return;
            }

            // /api/auth 요청에 대해서는 반드시 인증 헤더가 필요한데 이 값이 Null인 예외 발생
            throw new AuthorizationHeaderNotExistException();
        }

        
        // Reqeust 헤더의 "토큰타입 + 토큰" 에서 토큰 추출
        String jwtAccessToken = extractTokenFromAuthorizationHeader(authorizationHeaderValue);

        // 추출한 jwt AccessToken JWT Validator로 보내서 검증
        if (jwtValidator.validateJwtAccessToken(jwtAccessToken)) {

            // 정상적인 JWT AccessToken임이 확인 되었으면 ThreadLocal 저장소에 해당 토큰 저장 후 다음 필터로 통과 시켜줌
            try {
                jwtThreadLocalStorage.initJwtAccessTokenHolder(jwtAccessToken);
                log.info("ThreadLocal을 사용하는 요청, 반드시 리소스 정리 필요");
                chain.doFilter(request, response);
            }
            
            // JWTCheckFilter 이후 어떤 곳에서 예외가 발생 했더라도 반드시 돌아와서 ThreadLocal 저장소를 비움
            finally {
                jwtThreadLocalStorage.clearJwtAccessTokenHolder();
                log.info("ThreadLocal 리소스 정리 완료");
            }
        }
    }


    private String extractTokenFromAuthorizationHeader(String authorizationHeaderValue){

        if (authorizationHeaderValue.startsWith(BEARER_TOKEN_TYPE)){
            return authorizationHeaderValue.substring(7);
        }

        // 토큰 추출 중 토큰 타입이 Bearer 가 아닌 예외 발생
        throw new InvalidAuthorizationTokenTypeException();
    }
}
