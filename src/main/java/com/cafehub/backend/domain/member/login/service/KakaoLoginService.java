package com.cafehub.backend.domain.member.login.service;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.dto.response.KakaoOAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtTokenPayloadCreateDTO;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import com.cafehub.backend.domain.member.login.jwt.util.JwtProvider;
import com.cafehub.backend.domain.member.login.properties.kakao.KakaoProperties;
import com.cafehub.backend.domain.member.login.service.httpClient.OAuthHttpClient;
import com.cafehub.backend.domain.member.login.util.NicknameResolver;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoLoginService implements OAuth2LoginService {

    private final KakaoProperties properties;
    private final OAuthHttpClient httpClient;
    private final MemberRepository memberRepository;
    private final AuthInfoRepository authInfoRepository;

    private final JwtProvider jwtProvider;
    private final JwtPayloadReader jwtPayloadReader;



    @Override
    public String getLoginPageUrl() {
        return properties.getLoginUrlWithParams();
    }

    @Override
    public Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode, String provider) {

        KakaoOAuthTokenResponseDTO oAuthTokens = (KakaoOAuthTokenResponseDTO) httpClient.getOAuthTokens(authorizationCode, provider);
        log.info("카카오 인증서버에서 Access Token 받아오기 성공 ");

        KakaoUserResourceResponseDTO resources = (KakaoUserResourceResponseDTO) httpClient.getOAuthUserResources(oAuthTokens.getAccessToken(), provider);
        log.info("사용자 Resource 받아오기 성공");

        if(!authInfoRepository.existsByAppId(resources.getAppId())){

            signUp(resources);
            log.info("신규 회원 회원가입 성공");
        }

        return issueJwtTokens(resources.getAppId());
    }

    private void signUp(KakaoUserResourceResponseDTO resources) {

        Long appId = resources.getAppId();
        String email = resources.getKakaoAccount().getEmail();
        String nickname = resources.getKakaoAccount().getProfile().getNickname();
        String profileImgUrl = resources.getKakaoAccount().getProfile().getProfileImageUrl();


        nickname = NicknameResolver.adjustNicknameLength(nickname);
        while (memberRepository.existsByNickname(nickname)) nickname = NicknameResolver.adjustDuplicateNickname(nickname);

        AuthInfo newMemberAuthInfo = AuthInfo.from(appId, KAKAO_OAUTH_PROVIDER_NAME);

        Member newMember = Member.from(newMemberAuthInfo, nickname, email, profileImgUrl);

        memberRepository.save(newMember);
    }

    @Override
    public String getLogoutPageUrl(Long memberId) {

        log.info("사용자가 CafeHub만 로그아웃 할지, 카카오와 함께 로그아웃 할지 결정");

        return "https://kauth.kakao.com/oauth/logout?" +
                "client_id="+ properties.getClientId() + "&" +
                "logout_redirect_uri=" + properties.getLogoutRedirectUrl() + "&"
                + "state=kakao" + memberId;
    }

    @Override
    public void removeRefreshTokenOnLogout(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        AuthInfo authInfo = member.getAuthInfo();
        authInfo.deleteJwtRefreshTokenByLogout();
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

}