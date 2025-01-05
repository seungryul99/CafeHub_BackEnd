package com.cafehub.backend.common.env.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KakaoLoginPropertiesConfig {

    private final KakaoLoginPropertiesLoader kakaoLoginPropertiesLoader;

    @Bean
    public KakaoLoginProperties kakaoProperties(){
        return new KakaoLoginProperties(
                kakaoLoginPropertiesLoader.getLoginUrl(),
                kakaoLoginPropertiesLoader.getClientId(),
                kakaoLoginPropertiesLoader.getClientSecret(),
                kakaoLoginPropertiesLoader.getRedirectUri(),
                kakaoLoginPropertiesLoader.getLogoutRedirectUrl(),
                kakaoLoginPropertiesLoader.getLogoutUrl()
        );
    }
}
