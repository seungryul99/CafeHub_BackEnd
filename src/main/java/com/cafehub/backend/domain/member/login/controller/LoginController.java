package com.cafehub.backend.domain.member.login.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.env.login.LoginEnv;
import com.cafehub.backend.common.util.JwtThreadLocalStorageManager;
import com.cafehub.backend.domain.member.login.jwt.service.JwtReIssueService;
import com.cafehub.backend.domain.member.login.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.StringTokenizer;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController implements LoginControllerAPI{

    private final LoginEnv env;
    private final JwtThreadLocalStorageManager threadLocalStorageManager;
    private final JwtReIssueService jwtReIssueService;
    private final Map<String, OAuth2LoginService> oAuth2LoginServiceMap;


    @GetMapping("/api/member/login/{provider}")
    public ResponseEntity<Void> login(@PathVariable("provider") String provider){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + LOGIN_SERVICE_SUFFIX);
        log.info("사용자가 {}로 로그인 하기 버튼을 누름", provider);

        return ResponseEntity.status(FOUND)
                .header(LOCATION_HEADER, loginService.getLoginPageUrl())
                .build();
    }

    @GetMapping("/oauth/callback")
    public ResponseEntity<Void> OAuthCallback(@RequestParam ("code") String authorizationCode,
                                              @RequestParam ("state") String provider){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + LOGIN_SERVICE_SUFFIX);

        log.info("사용자가 카카오에 로그인했고 {}에서 CafeHub에 사용자를 통해서 리다이렉트로 콜백 성공", provider);

        Map<String, String> tokenMap = loginService.loginWithOAuthAndIssueJwt(authorizationCode, provider);

        log.info("{} 로그인 콜백 요청 처리완료" , provider);

        return ResponseEntity.status(FOUND)
                .header(SET_COOKIE_HEADER, JWT_ACCESS_TOKEN + "=" + tokenMap.get(JWT_ACCESS_TOKEN) + env.getJwtAccessCookieSetting())
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" + tokenMap.get(JWT_REFRESH_TOKEN) + env.getJwtRefreshCookieSetting())
                .header(LOCATION_HEADER, env.getFrontLoginSuccessUri())
                .build();
    }

    @PostMapping("/reissue/token")
    public ResponseEntity<ResponseDTO<Void>> reissueJwtTokens(@CookieValue("JwtRefreshToken") String jwtRefreshToken){

        Map<String, String> reIssueTokens = jwtReIssueService.reIssueJwt(jwtRefreshToken);

        String accessToken = reIssueTokens.get(JWT_ACCESS_TOKEN);
        String refreshToken = reIssueTokens.get(JWT_REFRESH_TOKEN);

        return ResponseEntity.status(200)
                .header(SET_COOKIE_HEADER, JWT_ACCESS_TOKEN + "=" + accessToken + env.getJwtAccessCookieSetting())
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" +  refreshToken + env.getJwtRefreshCookieSetting())
                .build();
    }

    @PostMapping("/api/member/logout")
    public ResponseEntity<ResponseDTO<?>> providerLogout(@CookieValue(JWT_REFRESH_TOKEN) String jwtRefreshToken){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(threadLocalStorageManager.getProvider() + LOGIN_SERVICE_SUFFIX);

        return ResponseEntity.ok(ResponseDTO.success(loginService.getLogoutPageUrl(threadLocalStorageManager.getMemberId(),jwtRefreshToken)));
    }

    @GetMapping("/serviceLogout")
    public ResponseEntity<ResponseDTO<Void>> serviceLogout(@RequestParam("state") StringTokenizer state){

        String provider = state.nextToken();
        Long memberId = Long.valueOf(state.nextToken());
        String jwtRefreshToken = state.nextToken();

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + LOGIN_SERVICE_SUFFIX);
        loginService.removeJwtRefreshToken(memberId,jwtRefreshToken);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" + env.getJwtRefreshCookieDelSetting())
                .header(LOCATION_HEADER, env.getFrontLogoutSuccessUri())
                .build();
    }
}