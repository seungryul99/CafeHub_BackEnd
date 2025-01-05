package com.cafehub.backend.common.env.kakao;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


// Q. 이거 @Component 없어도 상관 없을까?
@Getter
@ConfigurationProperties("kakao")
public class KakaoLoginPropertiesLoader {

    private final String loginUrl;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String logoutRedirectUrl;
    private final String logoutUrl;

    public KakaoLoginPropertiesLoader(String loginUrl, String clientId, String clientSecret, String redirectUri, String logoutRedirectUrl, String logoutUrl) {
        this.loginUrl = loginUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.logoutUrl = logoutUrl;
    }
}