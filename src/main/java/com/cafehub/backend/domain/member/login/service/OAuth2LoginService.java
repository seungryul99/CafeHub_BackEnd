package com.cafehub.backend.domain.member.login.service;


import java.util.Map;

public interface OAuth2LoginService {
    String getLoginPageUrl(String provider);

    Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode);

    String getLogoutPageUrl();

}
