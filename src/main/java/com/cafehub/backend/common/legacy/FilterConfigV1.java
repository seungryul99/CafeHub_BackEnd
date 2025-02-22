package com.cafehub.backend.common.legacy;


import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


//@Configuration
@RequiredArgsConstructor
public class FilterConfigV1 {

    private final ObjectMapper objectMapper;
    private final JwtValidator jwtValidator;
    private final JwtThreadLocalStorage jwtThreadLocalStorage;


    // [FeedBack] 필터를 조금 더 세분화 하면 동시에 많은 요청이 들어올 때 좋지 않나?
    // [FeedBack] CORS 필터랑 다른 필터 순서 이게 맞나?


//    @Bean
//    public FilterRegistrationBean<Filter> GlobalFilterExceptionHandleFilter() {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new GlobalFilterExceptionHandleFilter(objectMapper));
//        filterRegistrationBean.setOrder(0);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }
//
//
//    @Bean
//    public FilterRegistrationBean<Filter> corsCheckFilter() {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new PreflightRequestManageFilter());
//        filterRegistrationBean.setOrder(1);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }
//
//
//    // [FeedBack] 이거 필터 두 개 나누는 게 맞다
//    @Bean
//    public FilterRegistrationBean<Filter> loginCheckFilter() {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new JwtValidationFilterV1(jwtValidator,jwtThreadLocalStorage));
//        filterRegistrationBean.setOrder(2);
//        filterRegistrationBean.addUrlPatterns("/api/auth/*", "/api/optional-auth/*");
//        return filterRegistrationBean;
//    }

}