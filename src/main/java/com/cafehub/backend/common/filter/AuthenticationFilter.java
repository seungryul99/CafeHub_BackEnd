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

/**
 *   JWT 시크릿 키를 탈취 당하지 않았다면 JWT 검증 필터를 통과한 JWT로 부터 신뢰할 수 있는 회원 정보를 획득 할 수 있다.
 *   또한 JWT에 변경 가능성이 있는 회원의 정보는 넣지 않았다. 따라서 따로 MemebrRepository를 이용한 2차 검증은 해주지 않았다.
 *   다만 이럴 경우 차단된 회원 여부 확인이나 JWT에 없는 정보를 회원 인증정보 저장 객체에 저장 할 수 없다는 단점이 있다.
 */
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

        // request에서 사용자의 검증된 실제 Jwt 추출
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = String.valueOf(httpServletRequest.getAttribute("token"));


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