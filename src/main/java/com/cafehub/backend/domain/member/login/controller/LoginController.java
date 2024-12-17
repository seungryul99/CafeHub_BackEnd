package com.cafehub.backend.domain.member.login.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.member.login.jwt.service.JwtAuthService;
import com.cafehub.backend.domain.member.login.service.OAuth2LoginService;
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
public class LoginController implements LoginControllerAPI{

    private final JwtThreadLocalStorage jwtThreadLocalStorage;
    private final JwtAuthService jwtAuthService;
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
                .header(SET_COOKIE_HEADER, JWT_ACCESS_TOKEN + "=" + tokenMap.get(JWT_ACCESS_TOKEN) + JWT_ACCESS_TOKEN_SETTING)
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" +  tokenMap.get(JWT_REFRESH_TOKEN) + JWT_REFRESH_TOKEN_SETTING)
                .header(LOCATION_HEADER, FRONT_LOGIN_SUCCESS_URI)
                .build();
    }

    @PostMapping("/reissue/token")
    public ResponseEntity<ResponseDTO<Void>> reissueJwtTokens(@CookieValue("JwtRefreshToken") String jwtRefreshToken){

        Map<String, String> reIssueTokens = jwtAuthService.reIssueJwtAccessTokenWithRefreshToken(jwtRefreshToken);

        String accessToken = reIssueTokens.get(JWT_ACCESS_TOKEN);
        String refreshToken = reIssueTokens.get(JWT_REFRESH_TOKEN);


        return ResponseEntity.status(200)
                .header(SET_COOKIE_HEADER, JWT_ACCESS_TOKEN + "=" + accessToken + JWT_ACCESS_TOKEN_SETTING)
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" +  refreshToken + JWT_REFRESH_TOKEN_SETTING)
                .build();
    }


    @PostMapping("/api/auth/member/logout")
    public ResponseEntity<ResponseDTO<?>> providerLogout (){

        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(jwtThreadLocalStorage.getOAuthProviderNameFromJwt() + LOGIN_SERVICE_SUFFIX);

        return ResponseEntity.ok(ResponseDTO.success(loginService.getLogoutPageUrl(jwtThreadLocalStorage.getMemberIdFromJwt())));
    }


    @GetMapping("/serviceLogout")
    public ResponseEntity<ResponseDTO<Void>> serviceLogout(@RequestParam("state") String state){

        String provider = state.replaceAll("\\d.*", "");
        Long memberId = Long.parseLong(state.replaceAll("\\D", ""));


        OAuth2LoginService loginService = oAuth2LoginServiceMap.get(provider + LOGIN_SERVICE_SUFFIX);
        loginService.removeRefreshTokenOnLogout(memberId);

    
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(SET_COOKIE_HEADER, JWT_REFRESH_TOKEN + "=" + JWT_REFRESH_TOKEN_LOGOUT_SETTING)
                .header(LOCATION_HEADER, FRONT_LOGOUT_SUCCESS_URI)
                .build();
    }

}