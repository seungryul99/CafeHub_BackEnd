package com.cafehub.backend.domain.member.login.converter;

import com.cafehub.backend.domain.member.login.dto.request.KakaoOAuthTokenRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class MultiValueMapConverter {

    public static MultiValueMap<String, String> toMultivalueMap(KakaoOAuthTokenRequestDTO requestDTO){

        MultiValueMap<String, String> formDataParameters = new LinkedMultiValueMap<>();

        formDataParameters.add("grant_type", requestDTO.getGrantType());
        formDataParameters.add("client_id", requestDTO.getClientId());
        formDataParameters.add("redirect_uri", requestDTO.getRedirectUri());
        formDataParameters.add("code", requestDTO.getCode());
        formDataParameters.add("client_secret", requestDTO.getClientSecret());

        return formDataParameters;
    }
}
