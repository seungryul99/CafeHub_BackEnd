package com.cafehub.backend.domain.member.login.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoOAuthTokenResponseDTO {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty ("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;
}