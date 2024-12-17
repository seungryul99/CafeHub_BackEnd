package com.cafehub.backend.domain.member.login.properties.kakao;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("kakao")
public class KakaoPropertiesLoader {

    private final String loginUrl;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String logoutRedirectUrl;

    public KakaoPropertiesLoader(String loginUrl, String clientId, String clientSecret, String redirectUri, String logoutRedirectUrl) {
        this.loginUrl = loginUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.logoutRedirectUrl = logoutRedirectUrl;
    }
}