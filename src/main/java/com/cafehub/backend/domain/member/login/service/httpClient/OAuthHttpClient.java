package com.cafehub.backend.domain.member.login.service.httpClient;

import com.cafehub.backend.domain.member.login.dto.response.OAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.OAuthUserResourceResponseDTO;

/**
 *  해당 인터페이스의 역할은 OAuthLoginService에서 OAuth를 위해 Authorization Server나 Resource Server와 통신을 해야 할 때,
 *  스프링 서버를 Client로 만들어 통신을 하게 해주는 것임.
 *
 *  구현체로 RestClient, RestTemplate, WebClient등을 이용한 구현체들이 있음.
 */
public interface OAuthHttpClient {

    OAuthTokenResponseDTO getOAuthTokens(String authorizationCode, String provider);
    OAuthUserResourceResponseDTO getOAuthUserResources(String accessToken, String provider);
}
