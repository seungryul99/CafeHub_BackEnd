package com.cafehub.backend.domain.member.service;


import java.util.Map;

public interface OAuth2MemberService {
    String getLoginPageUrl(String provider);

    Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode);
}
