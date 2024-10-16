package com.cafehub.backend.config;


import com.cafehub.backend.common.filter.CorsFilter;
import com.cafehub.backend.common.filter.GlobalFilterExceptionHandleFilter;
import com.cafehub.backend.common.filter.jwt.JwtCheckFilter;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.member.jwt.JwtValidator;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtValidator jwtValidator;
    private final JwtThreadLocalStorage jwtThreadLocalStorage;


    @Bean
    public FilterRegistrationBean<Filter> GlobalFilterExceptionHandleFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new GlobalFilterExceptionHandleFilter());
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean<Filter> corsCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CorsFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtCheckFilter(jwtValidator,jwtThreadLocalStorage));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/api/auth/*", "/api/optional-auth/*");
        return filterRegistrationBean;
    }

}