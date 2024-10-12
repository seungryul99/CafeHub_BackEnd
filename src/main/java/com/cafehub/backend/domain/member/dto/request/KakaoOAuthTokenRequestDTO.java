package com.cafehub.backend.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

@Getter
public class KakaoOAuthTokenRequestDTO {

    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String code;
    private final String clientSecret;

    @Builder
    public KakaoOAuthTokenRequestDTO(String clientId, String redirectUri, String clientSecret, String code){

        this.grantType = "authorization_code";
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public MultiValueMap<String, String> convertAllFieldsToMultiValueMap() {

        MultiValueMap<String, String> formDataParameters = new LinkedMultiValueMap<>();

        formDataParameters.put("grant_type", Collections.singletonList(grantType));
        formDataParameters.put("client_id", Collections.singletonList(clientId));
        formDataParameters.put("redirect_uri", Collections.singletonList(redirectUri));
        formDataParameters.put("code", Collections.singletonList(code));
        formDataParameters.put("client_secret", Collections.singletonList(clientSecret));

        return formDataParameters;
    }
}
