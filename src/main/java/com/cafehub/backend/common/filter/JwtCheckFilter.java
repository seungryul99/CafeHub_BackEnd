package com.cafehub.backend.common.filter;

import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
import com.cafehub.backend.domain.member.jwt.JwtValidator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtCheckFilter implements Filter {

    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JWT CHECK FILTER 초기화");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;


        log.info("JWT Check Filter 에서 인증이 필요한 reqeust에 대해서 JWT 유효성 검사 시작");
        
        // Authorization 헤더에서 JWT Access 토큰 추출
        String authorizationHeader = "Bearer " + httpServletRequest.getHeader("Authorization");
        log.info("헤더에서 추출한 토큰 : " + authorizationHeader);
        String jwtAccessToken;


        if (authorizationHeader != null) {
            jwtAccessToken = authorizationHeader.substring(7); // "Bearer "의 길이는 7

            log.info("추출한 accessToken: {}", jwtAccessToken);

            if(jwtValidator.validateJwtAccessToken(jwtAccessToken)) {

                log.info("jwt Access Token 검증 성공");

                log.info("사용자 닉네임 : " + jwtPayloadReader.getNickname(jwtAccessToken));
                log.info("사용자 멤버 ID : " + jwtPayloadReader.getMemberId(jwtAccessToken));

                chain.doFilter(request, response);
            }
            else {

                log.info("jwt Access Token 검증 실패");
            }


        } else {
            log.info("Request 헤더에서 토큰을 추출하지 못함");
        }

    }
}
