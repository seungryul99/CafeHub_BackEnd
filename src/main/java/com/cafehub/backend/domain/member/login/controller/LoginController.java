package com.cafehub.backend.domain.member.login.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.member.login.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {


    public static final String Login_SERVICE_SUFFIX = "LoginService";

    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private final Map<String, OAuth2LoginService> oAuth2LoginServiceMap;


    @GetMapping("/api/member/login/{provider}")
    public ResponseEntity<Void> login(@PathVariable("provider") String provider){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + Login_SERVICE_SUFFIX);

        log.info("로그인 요청 발생, 사용자가 프론트에서 {}로 로그인 하기 버튼을 누름", provider);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", loginService.getLoginPageUrl(provider))
                .build();
    }




    @GetMapping("/oauth/callback")
    public ResponseEntity<Void> OAuthCallback(@RequestParam ("code") String authorizationCode,
                                              @RequestParam ("state") String provider){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + Login_SERVICE_SUFFIX);

        log.info("사용자가 카카오에 로그인했고 {}에서 CafeHub에 사용자를 통해서 리다이렉트로 콜백 성공", provider);

        Map<String, String> jwtTokens = loginService.loginWithOAuthAndIssueJwt(authorizationCode);

        String jwtAccessToken = jwtTokens.get("jwtAccessToken");
        String jwtRefreshToken = jwtTokens.get("jwtRefreshToken");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Set-Cookie", "JwtAccessToken=" + "Bearer " + jwtAccessToken + "; Path=/; Max-Age=3600; SameSite=Lax; Secure")
                .header("Set-Cookie", "JwtRefreshToken=" + "Bearer " +  jwtRefreshToken + "; Path=/; Max-Age=604800; SameSite=Lax; HttpOnly; Secure")
                .header("Location", "http://localhost:3000/OAuthCallback")
                .build();
    }



    @PostMapping("/api/auth/member/logout")
    public ResponseEntity<ResponseDTO<?>> providerLogout (){

        // 1. CafeHub말고 카카오 계정도 로그아웃 할지 연동시켜줘야함

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(jwtThreadLocalStorage.getOAuthProviderNameFromJwt() + Login_SERVICE_SUFFIX);

        return ResponseEntity.ok(ResponseDTO.success(loginService.getProviderLogoutPageUrl()));
    }

    @GetMapping("/serviceLogout")
    public ResponseEntity<ResponseDTO<Void>> serviceLogout(@RequestParam("state") String provider){

        // 확장을 위해 남겨둠, DB에서 사용자 AuthInfo 와 관련해서 OAuth2 Refresh Token 제거 등의 추후 처리가 필요하다던가 등등
        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + Login_SERVICE_SUFFIX);

    
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Set-Cookie", "JwtRefreshToken=; Path=/; Max-Age=0; SameSite=Lax; HttpOnly; Secure")
                .header("Location", "http://localhost:3000/Logout")
                .build();
    }

}