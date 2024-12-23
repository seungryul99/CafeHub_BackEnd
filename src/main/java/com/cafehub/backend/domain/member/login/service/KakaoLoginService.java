package com.cafehub.backend.domain.member.login.service;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.dto.response.KakaoOAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.login.jwt.util.JwtTokenManager;
import com.cafehub.backend.domain.member.login.properties.kakao.KakaoProperties;
import com.cafehub.backend.domain.member.login.service.httpClient.OAuthHttpClient;
import com.cafehub.backend.domain.member.login.util.NicknameResolver;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.KAKAO_OAUTH_PROVIDER_NAME;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoLoginService implements OAuth2LoginService {

    private final KakaoProperties properties;
    private final OAuthHttpClient httpClient;
    private final JwtTokenManager jwtTokenManager;
    private final MemberRepository memberRepository;
    private final AuthInfoRepository authInfoRepository;

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

        if(!authInfoRepository.existsByAppId(resources.getAppId())) signUp(resources);

        Member member = memberRepository.findMemberAndAuthInfoByAppId(resources.getAppId());
        return jwtTokenManager.issueJwtTokens(member,KAKAO_OAUTH_PROVIDER_NAME);
    }

    
    // [Refactor Point] OS + GC + JVM 을 조금더 공부해 보면 이걸 어떻게 처리 하는 게 바람직 할지 판단할 수 있음, 코드에 CS를 녹이는 법
    // + 꼭 Service만이 Repository를 호출해야 하는 건 아닌것 같다.
    private void signUp(KakaoUserResourceResponseDTO resources) {

        String nickname = resources.getKakaoAccount().getProfile().getNickname();

        nickname = NicknameResolver.adjustNicknameLength(nickname);
        while (memberRepository.existsByNickname(nickname)) nickname = NicknameResolver.adjustDuplicateNickname(nickname);

        AuthInfo authInfo = AuthInfo.from(resources.getAppId(), KAKAO_OAUTH_PROVIDER_NAME);
        Member member = Member.from(authInfo, nickname, resources.getKakaoAccount().getEmail(),
                                        resources.getKakaoAccount().getProfile().getProfileImageUrl());

        memberRepository.save(member);

        log.info("신규 회원 회원가입 성공");
    }

    @Override
    public String getLogoutPageUrl(Long memberId) {
        return properties.getLogoutUrlWithParams() + memberId;
    }

    @Override
    public void removeRefreshTokenOnLogout(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        member.getAuthInfo().deleteJwtRefreshTokenByLogout();
    }

}