package com.cafehub.backend.domain.member.login.service;


import java.util.Map;

public interface OAuth2LoginService {
    String getLoginPageUrl();
    Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode, String provider);

    String getLogoutPageUrl(Long memberId, String jwtRefreshToken);

    void removeJwtRefreshToken(Long memberId, String jwtRefreshToken);
}
