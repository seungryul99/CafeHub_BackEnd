package com.cafehub.backend.common.filter;


import com.cafehub.backend.common.util.JwtThreadLocalStorageManager;
import com.cafehub.backend.common.util.MemberAuthentication;
import com.cafehub.backend.domain.member.entity.Role;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OptionalAuthFilter implements Filter {

    private final JwtPayloadReader jwtPayloadReader;
    private final JwtThreadLocalStorageManager jwtThreadLocalStorageManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("OptionalAuthFilter Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("Optional Auth Request 처리");
        
        // request에서 사용자의 검증된 실제 Jwt Access Token 추출
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = String.valueOf(httpServletRequest.getAttribute("JwtAccessToken"));

        // 토큰이 없으면 비 로그인 회원의 Optional-auth요청으로 식별
        if (accessToken.equals("null")) {
            log.info("비 로그인 사용자의 Optional Auth Request 처리");
            chain.doFilter(request, response);
            return;
        }

        // 사용자의 실제 토큰으로 부터 사용자의 인증 정보를 추출
        MemberAuthentication authentication = new MemberAuthentication(jwtPayloadReader.getMemberId(accessToken),
                                                         Role.valueOf(jwtPayloadReader.getMemberRole(accessToken)),
                                                         jwtPayloadReader.getProvider(accessToken));

        // 사용자의 실제 인증 정보 ThreadLocal에 저장
        try{
            log.info("로그인 한 회원의 Optional Auth Request 처리");
            jwtThreadLocalStorageManager.setMemberAuthentication(authentication);
            chain.doFilter(request,response);
        }
        finally {
            jwtThreadLocalStorageManager.clear();
            log.info("자원 정리");
        }
    }

}
