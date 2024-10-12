package com.cafehub.backend.domain.member.service;

import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.dto.request.KakaoOAuthTokenRequestDTO;
import com.cafehub.backend.domain.member.dto.response.KakaoOAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.jwt.JwtMemberPayloadDTO;
import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
import com.cafehub.backend.domain.member.jwt.JwtProvider;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@Transactional
public class KakaoMemberService implements OAuth2MemberService {

    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;
    private final String loginPageUrl;

    private final RestClient restClient;
    private final MemberRepository memberRepository;
    private final AuthInfoRepository authInfoRepository;
    private final JwtProvider jwtProvider;
    private final JwtPayloadReader jwtPayloadReader;


    public KakaoMemberService(@Value("${kakao.loginUrl}") String kakaoLoginUrl,
                              @Value("${kakao.clientId}") String clientId,
                              @Value("${kakao.redirectUri}") String redirectUri,
                              @Value("${kakao.clientSecret}") String clientSecret,
                              RestClient restClient, MemberRepository memberRepository,
                              AuthInfoRepository authInfoRepository, JwtProvider jwtProvider, JwtPayloadReader jwtPayloadReader) {

        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.restClient = restClient;
        this.authInfoRepository = authInfoRepository;
        this.jwtPayloadReader = jwtPayloadReader;
        this.loginPageUrl = kakaoLoginUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public String getLoginPageUrl(String provider) {

        // 추후 csrf 공격 방지 목적을 위한 OAuth의 state 파라미터를 목적에 맞게 사용할 예정임
        // 자세한 내용은 개발 로그에
        return loginPageUrl + "&state=" + provider;
    }


    @Override
    public Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode) {

        KakaoOAuthTokenResponseDTO tokenResponse = getOAuthTokens(authorizationCode);

        String accessToken = tokenResponse.getAccessToken();
        log.info("카카오 인증서버에서 Access Token 받아오기 성공: " + accessToken.substring(0, 5));

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
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"))
                .body(tokenRequestDTO.convertAllFieldsToMultiValueMap())
                .retrieve()
                .body(KakaoOAuthTokenResponseDTO.class);
    }

    private KakaoUserResourceResponseDTO getKakaoMemberResourceByAccessToken(String accessToken) {

        log.info("AccessToken을 통해서 사용자의 카카오 상 정보를 받아옴");

        return restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
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
         *  추후 성능 테스트 때 성능 직접 비교 하면서 Join 방식과 비교후 바꿀 생각, 자세한건 개발로그
         *  
         *  일단은 임시로 AuthInfo로만 검증
         */

        AuthInfo authInfo = authInfoRepository.findByAppId(appId);

        if (authInfo == null) {

            AuthInfo newMemberAuthInfo = AuthInfo.builder()
                    .appId(appId)
                    .provider("kakao")
                    .build();


            Member newMember = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .profileImg(new Image(profileImageUrl))
                    .authInfo(newMemberAuthInfo)
                    .build();

            memberRepository.save(newMember);
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

        Member member = memberRepository.findByAuthInfo(authInfo);

        JwtMemberPayloadDTO jwtMemberPayloadDTO = JwtMemberPayloadDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();


        String jwtAccessToken = jwtProvider.createJwtAccessToken(jwtMemberPayloadDTO);
        log.info("JWT ACCESS TOKEN 발급 성공!");
        String jwtRefreshToken = jwtProvider.createJwtRefreshToken(jwtMemberPayloadDTO);
        log.info("JWT REFRESH TOKEN 발급 성공!");


        authInfo.updateAuthInfo(jwtRefreshToken, jwtPayloadReader.getExpiration(jwtRefreshToken));


        Map<String, String> jwtTokens = new HashMap<>();
        jwtTokens.put("jwtAccessToken", jwtAccessToken);
        jwtTokens.put("jwtRefreshToken", jwtRefreshToken);

        return jwtTokens;
    }

}