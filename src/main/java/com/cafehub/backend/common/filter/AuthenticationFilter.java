package com.cafehub.backend.common.filter;

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
public class AuthenticationFilter implements Filter {

    private final JwtPayloadReader jwtPayloadReader;
    private final JwtThreadLocalStorageManager jwtThreadLocalStorageManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AuthenticationFilter Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // request에서 사용자의 검증된 실제 Jwt Access Token 추출
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = String.valueOf(httpServletRequest.getAttribute("JwtAccessToken"));

        // 사용자의 실제 토큰으로 부터 사용자의 인증 정보를 추출
        MemberAuthentication authentication = new MemberAuthentication(jwtPayloadReader.getMemberId(accessToken),
                                                        Role.valueOf(jwtPayloadReader.getMemberRole(accessToken)),
                                                        jwtPayloadReader.getProvider(accessToken));

        // 사용자의 실제 인증 정보 ThreadLocal에 저장
        try{
            log.info("ThreadLocal을 사용하는 Auth Request 발생");
            jwtThreadLocalStorageManager.setMemberAuthentication(authentication);
            chain.doFilter(request,response);
        }
        finally {
            jwtThreadLocalStorageManager.clear();
            log.info("자원 정리");
        }
    }
}