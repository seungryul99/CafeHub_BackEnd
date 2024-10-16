package com.cafehub.backend.domain.member.controller;

import com.cafehub.backend.domain.member.service.OAuth2MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    public static final String MEMBER_SERVICE_SUFFIX = "MemberService";

    private final Map<String,OAuth2MemberService> oAuth2MemberServiceMap;


    @GetMapping("/api/member/login/{provider}")
    public ResponseEntity<Void> login(@PathVariable("provider") String provider){

        OAuth2MemberService memberService = oAuth2MemberServiceMap.get(provider + MEMBER_SERVICE_SUFFIX);

        log.info("로그인 요청 발생, 사용자가 프론트에서 {}로 로그인 하기 버튼을 누름", provider);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", memberService.getLoginPageUrl(provider))
                .build();
    }




    @GetMapping("/oauth/callback")
    public ResponseEntity<Void> OAuthCallback(@RequestParam ("code") String authorizationCode,
                                              @RequestParam ("state") String provider){

        OAuth2MemberService memberService = oAuth2MemberServiceMap.get(provider + MEMBER_SERVICE_SUFFIX);

        log.info("사용자가 카카오에 로그인했고 {}에서 CafeHub에 사용자를 통해서 리다이렉트로 콜백 성공", provider);

        Map<String, String> jwtTokens = memberService.loginWithOAuthAndIssueJwt(authorizationCode);

        String jwtAccessToken = jwtTokens.get("jwtAccessToken");
        String jwtRefreshToken = jwtTokens.get("jwtRefreshToken");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Set-Cookie", "JwtAccessToken=" + "Bearer " + jwtAccessToken + "; Path=/; Max-Age=3600; SameSite=Lax; Secure")
                .header("Set-Cookie", "JwtRefreshToken=" + "Bearer " +  jwtRefreshToken + "; Path=/; Max-Age=604800; SameSite=Lax; HttpOnly; Secure")
                .header("Location", "http://localhost:3000/OAuthCallback")
                .build();
    }

}