package com.cafehub.backend.domain.member.controller;

import com.cafehub.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/api/member/login")
    public ResponseEntity<Void> login(){

        log.info("프론트 쪽 로그인 요청 발생, 카카오톡으로 로그인 하기 버튼을 누른 상황");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", memberService.getKakaoLoginPageUrl())
                .build();
    }


    @GetMapping("/kakao/oauth/callback")
    public ResponseEntity<?> OAuthCallback(@RequestParam ("code") String authorizationCode){

        log.info("사용자가 카카오에 로그인했고 카카오에서 CafeHub에 사용자를 통해서 리다이렉트로 콜백 성공");

        Map<String, String> jwtTokens = memberService.getKakoResourceServerAccessToken(authorizationCode);

        String jwtAccessToken = jwtTokens.get("jwtAccessToken");
        String jwtRefreshToken = jwtTokens.get("jwtRefreshToken");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Set-Cookie", "JwtAccessToken=" + jwtAccessToken + "; Path=/; Max-Age=3600; SameSite=Lax; Secure")
                .header("Set-Cookie", "JwtRefreshToken=" + jwtRefreshToken + "; Path=/; Max-Age=604800; SameSite=Lax; HttpOnly; Secure")
                .header("Location", "http://localhost:3000/OAuthCallback")
                .build();
    }
}