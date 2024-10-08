package com.cafehub.backend.config;


import com.cafehub.backend.domain.member.jwt.JwtCheckFilter;
import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
import com.cafehub.backend.domain.member.jwt.JwtValidator;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 임시 Cors 설정 파일 추가, 추후 삭제 예정

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;



    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtCheckFilter(jwtValidator,jwtPayloadReader));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/api/auth/*", "/api/cafe/*");
        return filterRegistrationBean;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 노트북 React 실행 시 , 프론트 배포된 서버에서 요청날아올 시 ,
                .allowedOrigins("http://localhost:3000", "https://main.d3fr2u7nicdqc9.amplifyapp.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
