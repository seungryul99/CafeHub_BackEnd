package com.cafehub.backend.common.filter;

import com.cafehub.backend.common.env.cors.CorsProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CorsFilter implements Filter {

    private final CorsProperties corsProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("preflightRequestManageFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        corsConfig(httpServletResponse);

        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            log.info("preflight request 발생");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            log.info("실제 request 도착");
            chain.doFilter(request, response);
        }
    }

    private void corsConfig(HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", corsProperties.getAllowOrigin());
        response.setHeader("Access-Control-Allow-Methods", corsProperties.getAllowMethods());
        response.setHeader("Access-Control-Allow-Headers", corsProperties.getAllowHeaders());
        response.setHeader("Access-Control-Allow-Credentials", corsProperties.getAllowCredentials());
        response.setHeader("Access-Control-Max-Age", corsProperties.getPreflightCacheAge());
    }
}
