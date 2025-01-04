package com.cafehub.backend.domain.member.login.service.httpClient;

import com.cafehub.backend.domain.member.login.dto.response.OAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.OAuthUserResourceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.REST_CLIENT_MANAGER_SUFFIX;


@Component
@RequiredArgsConstructor
public class OAuthHttpRestClient implements OAuthHttpClient{

    private final Map<String,OAuthRestClientProvider> oAuthRestClientProviderMap;

    @Override
    public OAuthTokenResponseDTO getOAuthTokens(String authorizationCode, String provider) {

        return oAuthRestClientProviderMap.get(provider + REST_CLIENT_MANAGER_SUFFIX).getOAuthTokenResponseDTO(authorizationCode);
    }

    @Override
    public OAuthUserResourceResponseDTO getOAuthUserResources(String accessToken, String provider) {
        return oAuthRestClientProviderMap.get(provider + REST_CLIENT_MANAGER_SUFFIX).getOAuthUserResourceResponseDTO(accessToken);
    }
}
