package com.cafehub.backend.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.cafehub.backend.common.constants.CafeHubConstants.CORS_ALLOW_ORIGIN;


@Slf4j
public class CorsFilter implements Filter {

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

        response.setHeader("Access-Control-Allow-Origin", CORS_ALLOW_ORIGIN);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "10800");
    }
}
