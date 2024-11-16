package com.cafehub.backend.domain.member.login.service;

import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.dto.request.KakaoOAuthTokenRequestDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoOAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import com.cafehub.backend.domain.member.login.jwt.util.JwtProvider;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtTokenPayloadCreateDTO;
import com.cafehub.backend.domain.member.login.repository.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;


@Slf4j
@Service
@Transactional
public class KakaoLoginService implements OAuth2LoginService {

    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;
    private final String loginPageUrl;
    private final String logoutRedirectUrl;

    private final RestClient restClient;
    private final LoginRepository loginRepository;
    private final AuthInfoRepository authInfoRepository;
    private final JwtProvider jwtProvider;
    private final JwtPayloadReader jwtPayloadReader;


    public KakaoLoginService(@Value("${kakao.loginUrl}") String kakaoLoginUrl,
                             @Value("${kakao.clientId}") String clientId,
                             @Value("${kakao.redirectUri}") String redirectUri,
                             @Value("${kakao.clientSecret}") String clientSecret,
                             @Value("${kakao.logoutRedirectUrl}") String logoutRedirectUrl,
                             RestClient restClient, LoginRepository loginRepository,
                             AuthInfoRepository authInfoRepository, JwtProvider jwtProvider, JwtPayloadReader jwtPayloadReader) {

        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.restClient = restClient;
        this.authInfoRepository = authInfoRepository;
        this.jwtPayloadReader = jwtPayloadReader;
        this.loginPageUrl = kakaoLoginUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
        this.loginRepository = loginRepository;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public String getLoginPageUrl(String provider) {
        return loginPageUrl + "&state=" + provider;
    }


    @Override
    public Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode) {

        KakaoOAuthTokenResponseDTO tokenResponse = getOAuthTokens(authorizationCode);

        String accessToken = tokenResponse.getAccessToken();
        log.info("카카오 인증서버에서 Access Token 받아오기 성공: " + accessToken.substring(0, 3));

        KakaoUserResourceResponseDTO userInfo = getKakaoMemberResourceByAccessToken(accessToken);

        createMemberFromUserInfo(userInfo);

        return issueJwtTokens(userInfo.getAppId());
    }



    private KakaoOAuthTokenResponseDTO getOAuthTokens(String authorizationCode) {

        log.info("카카오 인증 서버에 code 발송, AccessToken + RefreshToken과 부가 정보들을 받아옴");

        KakaoOAuthTokenRequestDTO tokenRequestDTO = KakaoOAuthTokenRequestDTO.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(authorizationCode)
                .redirectUri(redirectUri)
                .build();


        return restClient.post()
                         .uri(KAKAO_OAUTH_TOKEN_REQUEST_URL)
                         .contentType(KAKAO_OAUTH_TOKEN_CONTENT_TYPE)
                         .body(tokenRequestDTO.convertAllFieldsToMultiValueMap())
                         .retrieve()
                         .body(KakaoOAuthTokenResponseDTO.class);
    }

    private KakaoUserResourceResponseDTO getKakaoMemberResourceByAccessToken(String accessToken) {

        log.info("OAuth AccessToken을 통해서 사용자의 카카오 상 정보를 받아옴");

        return restClient.get()
                .uri(KAKAO_USER_INFO_API_URL)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + accessToken)
                .retrieve()
                .body(KakaoUserResourceResponseDTO.class);
    }

    private void createMemberFromUserInfo(KakaoUserResourceResponseDTO userInfo) {

        log.info("신규 회원 체크 후 회원가입");

        Long appId = userInfo.getAppId();
        String nickname = userInfo.getKakaoAccount().getProfile().getNickname();
        String profileImageUrl = userInfo.getKakaoAccount().getProfile().getProfileImageUrl();
        String email = userInfo.getKakaoAccount().getEmail();


        /**
         *  AuthInfo의 appId를 통해서 AuthInfo 조회 후 해당 AuthInfo로 MemberExist 확인
         *  추후 성능 테스트 때 성능 직접 비교 하면서 Join 방식과 비교후 바꿀 생각
         *  
         *  일단은 임시로 AuthInfo로만 검증
         */

        AuthInfo authInfo = authInfoRepository.findByAppId(appId);

        if (authInfo == null) {

            AuthInfo newMemberAuthInfo = AuthInfo.builder()
                    .appId(appId)
                    .provider(KAKAO_OAUTH_PROVIDER_NAME)
                    .build();


            Member newMember = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .profileImg(profileImageUrl != null ? new Image(profileImageUrl) : new Image("DefaultImage"))
                    .authInfo(newMemberAuthInfo)
                    .build();

            loginRepository.save(newMember);
            log.info("회원가입 성공");

            return;
        }

        log.info("이미 회원가입 한 적 있는 회원입니다.");
    }


    private Map<String, String> issueJwtTokens(Long appId) {

        log.info("JWT 토큰들 발급 진행");

        /**
         *  해당 부분도 AuthInfo를 찾아오는 로직이 createMemberFromUserInfo에서 사용되고 있어 DB에 같은 요청을 날리는게 비효율 적이지만
         *  추후 성능테스트시 위의 AuthInfo를 찾아오는 메서드가 삭제될 수 있기 떄문에 같은 로직이지만 분리해서 중복을 허용함.
         *
         *  해당 부분도 성능 테스트 환경이 갖춰지면 테스트후 조금 더 수정될 예정
         */

        AuthInfo authInfo = authInfoRepository.findByAppId(appId);
        log.info("정상 동작 확인 " + authInfo.getAppId().toString());

        Member member = loginRepository.findByAuthInfo(authInfo);

        JwtTokenPayloadCreateDTO payload = JwtTokenPayloadCreateDTO.builder()
                .memberId(member.getId())
                .provider(KAKAO_OAUTH_PROVIDER_NAME)
                .build();


        String jwtAccessToken = jwtProvider.createJwtAccessToken(payload);
        log.info("JWT ACCESS TOKEN 발급 성공!");
        String jwtRefreshToken = jwtProvider.createJwtRefreshToken(payload);
        log.info("JWT REFRESH TOKEN 발급 성공!");

        authInfo.updateAuthInfoByJwtIssue(jwtRefreshToken, jwtPayloadReader.getExpiration(jwtRefreshToken));


        return Map.of(
                JWT_ACCESS_TOKEN, jwtAccessToken,
                JWT_REFRESH_TOKEN, jwtRefreshToken
        );
    }



    @Override
    public String getLogoutPageUrl() {

        log.info("사용자가 카카오 계정 로그아웃 할지 선택");

        return "https://kauth.kakao.com/oauth/logout?" +
                "client_id="+ clientId + "&" +
                "logout_redirect_uri=" + logoutRedirectUrl + "&"
                + "state=kakao";
    }


}