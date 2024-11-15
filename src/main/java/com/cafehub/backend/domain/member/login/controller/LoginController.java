package com.cafehub.backend.domain.member.login.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.member.login.jwt.service.JwtAuthService;
import com.cafehub.backend.domain.member.login.service.OAuth2LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private final Map<String, OAuth2LoginService> oAuth2LoginServiceMap;

    private final JwtAuthService jwtAuthService;


    @GetMapping("/api/member/login/{provider}")
    public ResponseEntity<Void> login(@PathVariable("provider") String provider){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + Login_SERVICE_SUFFIX);

        log.info("사용자가 {}로 로그인 하기 버튼을 누름", provider);

        return ResponseEntity.status(FOUND)
                .header(LOCATION_HEADER, loginService.getLoginPageUrl(provider))
                .build();
    }


    @GetMapping("/oauth/callback")
    public ResponseEntity<Void> OAuthCallback(@RequestParam ("code") String authorizationCode,
                                              @RequestParam ("state") String provider){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + Login_SERVICE_SUFFIX);

        log.info("사용자가 카카오에 로그인했고 {}에서 CafeHub에 사용자를 통해서 리다이렉트로 콜백 성공", provider);

        Map<String, String> jwtTokens = loginService.loginWithOAuthAndIssueJwt(authorizationCode);

        String jwtAccessToken = jwtTokens.get(JWT_ACCESS_TOKEN);
        String jwtRefreshToken = jwtTokens.get(JWT_REFRESH_TOKEN);


        return ResponseEntity.status(FOUND)
                .header(SET_COOKIE_HEADER, JWT_ACCESS_TOKEN + "=" + jwtAccessToken + JWT_ACCESS_TOKEN_SETTING)
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" +  jwtRefreshToken + JWT_REFRESH_TOKEN_SETTING)
                .header(LOCATION_HEADER, FRONT_LOGIN_SUCCESS_URI)
                .build();
    }



    @PostMapping("/api/auth/member/logout")
    public ResponseEntity<ResponseDTO<?>> providerLogout (){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(jwtThreadLocalStorage.getOAuthProviderNameFromJwt() + Login_SERVICE_SUFFIX);

        return ResponseEntity.ok(ResponseDTO.success(loginService.getLogoutPageUrl()));
    }

    @GetMapping("/serviceLogout")
    public ResponseEntity<ResponseDTO<Void>> serviceLogout(@RequestParam("state") String provider){

        // 확장을 위해 남겨둠, DB에서 사용자 AuthInfo 와 관련해서 OAuth2 Refresh Token 제거 등의 추후 처리가 필요하다던가 등등
        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + Login_SERVICE_SUFFIX);

    
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(SET_COOKIE_HEADER, "JwtRefreshToken=; Path=/; Max-Age=0; SameSite=Lax; HttpOnly; Secure")
                .header(LOCATION_HEADER, "http://localhost:3000/Logout")
                .build();
    }



    @PostMapping("/reissue/token")
    public ResponseEntity<ResponseDTO<Void>> reissueJwtTokens(@CookieValue("JwtRefreshToken") String jwtRefreshToken){


        Map<String, String> reIssueTokens = jwtAuthService.reIssueJwtAccessTokenWithRefreshToken(jwtRefreshToken);

        String reIssueAccessToken = reIssueTokens.get(JWT_ACCESS_TOKEN);
        String reIssueRefreshToken = reIssueTokens.get(JWT_REFRESH_TOKEN);


        return ResponseEntity.status(200)
                .header(SET_COOKIE_HEADER, JWT_ACCESS_TOKEN + "=" + reIssueAccessToken + JWT_ACCESS_TOKEN_SETTING)
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" +  reIssueRefreshToken + JWT_REFRESH_TOKEN_SETTING)
                .build();
    }

}