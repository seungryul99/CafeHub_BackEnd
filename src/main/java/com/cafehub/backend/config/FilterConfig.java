package com.cafehub.backend.config;


import com.cafehub.backend.common.filter.CorsFilter;
import com.cafehub.backend.common.filter.JwtCheckFilter;
import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
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
    private final JwtPayloadReader jwtPayloadReader;


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
        filterRegistrationBean.setFilter(new JwtCheckFilter(jwtValidator,jwtPayloadReader));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/api/auth/*");
        return filterRegistrationBean;
    }

}