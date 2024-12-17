package com.cafehub.backend.domain.member.login.service.httpClient;

import com.cafehub.backend.domain.member.login.dto.response.OAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.OAuthUserResourceResponseDTO;

public interface OAuthRestClientProvider {

    OAuthTokenResponseDTO getOAuthTokenResponseDTO(String authorizationCode);
    OAuthUserResourceResponseDTO getOAuthUserResourceResponseDTO(String accessToken);
}
