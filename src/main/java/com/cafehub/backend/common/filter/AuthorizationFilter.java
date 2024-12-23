package com.cafehub.backend.common.filter;

import com.cafehub.backend.domain.member.entity.Role;
import com.cafehub.backend.domain.member.login.exception.AuthorizationException;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter implements Filter {

    private final JwtThreadLocalStorageManager jwtThreadLocalStorageManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AuthorizationFilter Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // ThreadLocal에 저장된 MemberAuthentication 정보 중 Member Role을 가져옴
        if (jwtThreadLocalStorageManager.getMemberRole().equals(Role.ADMIN)) {
            log.info("관리자 권한 확인 완료");
            chain.doFilter(request,response);
        }
        else {
            throw new AuthorizationException();
        }
    }
}
