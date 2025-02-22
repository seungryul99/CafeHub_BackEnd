package com.cafehub.backend.config;

import com.cafehub.backend.common.filter.*;
import com.cafehub.backend.common.env.cors.CorsProperties;
import com.cafehub.backend.common.util.JwtThreadLocalStorageManager;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final ObjectMapper objectMapper;
    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;
    private final JwtThreadLocalStorageManager jwtThreadLocalStorageManager;
    private final CorsProperties corsProperties;

    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CorsFilter(corsProperties));
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> globalFilterExceptionHandleFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new GlobalFilterExceptionHandleFilter(objectMapper));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> jwtValidationFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtValidationFilter(jwtValidator));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/api/auth/*","/api/optional-auth/*","/swagger-ui.html", "/swagger-ui/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> jwtRefreshTokenValidationFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtRefreshTokenValidationFilter(jwtValidator));
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/reissue/token", "/api/member/logout");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> authenticationFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthenticationFilter(jwtPayloadReader,jwtThreadLocalStorageManager));
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.addUrlPatterns("/api/auth/*", "/swagger-ui.html", "/swagger-ui/*","/reissue/token", "/api/member/logout");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> optionalAuthFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new OptionalAuthFilter(jwtPayloadReader,jwtThreadLocalStorageManager));
        filterRegistrationBean.setOrder(5);
        filterRegistrationBean.addUrlPatterns("/api/optional-auth/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> authorizationFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthorizationFilter(jwtThreadLocalStorageManager));
        filterRegistrationBean.setOrder(6);
        filterRegistrationBean.addUrlPatterns("/swagger-ui.html", "/swagger-ui/*");
        return filterRegistrationBean;
    }
}
