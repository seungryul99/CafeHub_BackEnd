package com.cafehub.backend.common.env.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtPropertiesConfig {

    private final JwtPropertiesLoader jwtPropertiesLoader;

    @Bean
    public JwtProperties jwtProperties(){
        return new JwtProperties(
                jwtPropertiesLoader.getSecret(),
                jwtPropertiesLoader.getAccessTokenExpirationMs(),
                jwtPropertiesLoader.getRefreshTokenExpirationMs()
        );
    }
}
