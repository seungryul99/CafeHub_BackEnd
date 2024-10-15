package com.cafehub.backend.common.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GlobalFilterExceptionHandleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Global FilterException Handle FILTER 초기화");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("모든 요청은 이곳을 지나감");
        chain.doFilter(request, response);
        log.info("모든 응답은 이곳을 지나감");
    }
}
