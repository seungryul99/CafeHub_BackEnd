package com.cafehub.backend.domain.member.login.properties.kakao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoPropertiesConfig {

    private final KakaoPropertiesLoader kakaoPropertiesLoader;

    public KakaoPropertiesConfig(KakaoPropertiesLoader kakaoPropertiesLoader) {
        this.kakaoPropertiesLoader = kakaoPropertiesLoader;
    }

    @Bean
    public KakaoProperties kakaoProperties(){
        return new KakaoProperties(
                kakaoPropertiesLoader.getLoginUrl(),
                kakaoPropertiesLoader.getClientId(),
                kakaoPropertiesLoader.getClientSecret(),
                kakaoPropertiesLoader.getRedirectUri(),
                kakaoPropertiesLoader.getLogoutRedirectUrl()
        );
    }
}
