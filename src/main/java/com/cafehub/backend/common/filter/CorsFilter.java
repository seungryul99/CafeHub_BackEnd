package com.cafehub.backend.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class CorsFilter implements Filter {

    private static final String CORS_ALLOW_ORIGIN = "https://main.d3fr2u7nicdqc9.amplifyapp.com/";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("CORS FILTER Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", CORS_ALLOW_ORIGIN);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Max-Age", "10800");


        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            log.info("preflight request 발생");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            log.info("preflight request 처리");
        } else {
            log.info("실제 request 도착");
            chain.doFilter(request, response);
        }
    }
}
