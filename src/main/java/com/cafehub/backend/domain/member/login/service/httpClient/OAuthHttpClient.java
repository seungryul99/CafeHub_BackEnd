package com.cafehub.backend.domain.member.login.service.httpClient;

import com.cafehub.backend.domain.member.login.dto.response.OAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.OAuthUserResourceResponseDTO;

public interface OAuthHttpClient {

    OAuthTokenResponseDTO getOAuthTokens(String authorizationCode, String provider);
    OAuthUserResourceResponseDTO getOAuthUserResources(String accessToken, String provider);
}
