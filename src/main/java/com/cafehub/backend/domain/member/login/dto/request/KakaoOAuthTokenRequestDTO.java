package com.cafehub.backend.domain.member.login.dto.request;

import com.cafehub.backend.common.env.kakao.KakaoLoginProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoOAuthTokenRequestDTO {

    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String code;
    private final String clientSecret;

    @Builder(access = AccessLevel.PRIVATE)
    private KakaoOAuthTokenRequestDTO(String clientId, String redirectUri, String clientSecret, String code){

        this.grantType = "authorization_code";
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public static KakaoOAuthTokenRequestDTO from(String authorizationCode, KakaoLoginProperties properties){

        return KakaoOAuthTokenRequestDTO.builder()
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .code(authorizationCode)
                .redirectUri(properties.getRedirectUri())
                .build();
    }
}
