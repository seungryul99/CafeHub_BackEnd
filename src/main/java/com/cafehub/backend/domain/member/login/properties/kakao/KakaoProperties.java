package com.cafehub.backend.domain.member.login.properties.kakao;


import lombok.Getter;

@Getter
public class KakaoProperties {

    private final String loginUrl;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String logoutRedirectUrl;
    private final String loginUrlWithParams;

    public KakaoProperties(String loginUrl, String clientId,
                           String clientSecret, String redirectUri,
                           String logoutRedirectUrl) {

        this.loginUrl = loginUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.loginUrlWithParams = buildLoginUrlWithParams();
    }

    public String buildLoginUrlWithParams(){
        return  loginUrl +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code"+
                "&state=kakao";
    }
}
