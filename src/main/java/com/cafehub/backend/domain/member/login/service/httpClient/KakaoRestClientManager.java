package com.cafehub.backend.domain.member.login.service.httpClient;

import com.cafehub.backend.domain.member.login.converter.MultiValueMapConverter;
import com.cafehub.backend.domain.member.login.dto.request.KakaoOAuthTokenRequestDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoOAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.OAuthUserResourceResponseDTO;
import com.cafehub.backend.common.env.kakao.KakaoLoginProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Component
@RequiredArgsConstructor
public class KakaoRestClientManager implements OAuthRestClientProvider{

    private final RestClient restClient;
    private final KakaoLoginProperties properties;

    @Override
    public KakaoOAuthTokenResponseDTO getOAuthTokenResponseDTO(String authorizationCode){

        KakaoOAuthTokenRequestDTO oAuthTokenRequestDTO = KakaoOAuthTokenRequestDTO.from(authorizationCode,properties);

        return restClient.post()
                .uri(KAKAO_OAUTH_TOKEN_REQUEST_URL)
                .contentType(KAKAO_OAUTH_TOKEN_CONTENT_TYPE)
                .body(MultiValueMapConverter.toMultivalueMap(oAuthTokenRequestDTO))
                .retrieve()
                .body(KakaoOAuthTokenResponseDTO.class);
    }

    @Override
    public OAuthUserResourceResponseDTO getOAuthUserResourceResponseDTO(String accessToken) {

        return restClient.get()
                .uri(KAKAO_USER_INFO_API_URL)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + accessToken)
                .retrieve()
                .body(KakaoUserResourceResponseDTO.class);
    }
}
