package com.cafehub.backend.domain.member.login.service;

import com.cafehub.backend.common.properties.kakaoLogin.KakaoLoginProperties;
import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.dto.response.KakaoOAuthTokenResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenBlockedException;
import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenNotExistException;
import com.cafehub.backend.domain.member.login.jwt.util.JwtTokenManager;
import com.cafehub.backend.domain.member.login.service.httpClient.OAuthHttpClient;
import com.cafehub.backend.domain.member.login.util.NicknameResolver;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import com.cafehub.backend.domain.member.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;



@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoLoginService implements OAuth2LoginService {

    private final KakaoLoginProperties properties;
    private final OAuthHttpClient httpClient;
    private final JwtTokenManager jwtTokenManager;
    private final MemberRepository memberRepository;
    private final AuthInfoRepository authInfoRepository;
    private final RedisRepository redisRepository;

    @Override
    public String getLoginPageUrl() {
        return properties.getLoginUrlWithParams();
    }

    @Override
    public Map<String, String> loginWithOAuthAndIssueJwt(String authorizationCode, String provider) {

        KakaoOAuthTokenResponseDTO oAuthTokens = (KakaoOAuthTokenResponseDTO) httpClient.getOAuthTokens(authorizationCode, provider);
        log.info("{} 인증서버에서 Access Token 받아오기 성공 ", provider);

        KakaoUserResourceResponseDTO resources = (KakaoUserResourceResponseDTO) httpClient.getOAuthUserResources(oAuthTokens.getAccessToken(), provider);
        log.info("사용자 Resource 받아오기 성공");

        if(!authInfoRepository.existsByAppId(resources.getAppId())) signUp(resources, provider);

        Member member = memberRepository.findMemberAndAuthInfoByAppId(resources.getAppId());
        return jwtTokenManager.issueJwtTokens(member,provider);
    }

    private void signUp(KakaoUserResourceResponseDTO resources, String provider) {

        String nickname = resources.getKakaoAccount().getProfile().getNickname();
        String email = resources.getKakaoAccount().getEmail();
        String profileImgUrl = resources.getKakaoAccount().getProfile().getProfileImageUrl();

        nickname = NicknameResolver.adjustNicknameLength(nickname);
        while (memberRepository.existsByNickname(nickname)) nickname = NicknameResolver.adjustDuplicateNickname(nickname);

        AuthInfo authInfo = AuthInfo.from(resources.getAppId(), provider);
        Member member = Member.from(authInfo, nickname, email,profileImgUrl);

        memberRepository.save(member);

        log.info("신규 회원 회원가입 성공");
    }

    @Override
    public String getLogoutPageUrl(Long memberId, String jwtRefreshToken) {
        
        // 쿼리스트링에서 '+'는 공백을 의미함
        return properties.getLogoutUrlWithParams() + "+" + memberId + "+" + jwtRefreshToken;
    }

    @Override
    public void removeJwtRefreshToken(Long memberId, String jwtRefreshToken) {

        // 사용자가 보내온 리프레시 토큰의 화이트리스트 ttl이 이미 만료되어서 삭제해줄 필요가 없는 경우 레디스 저장소에서 별도의 삭제가 필요 없음
        String whiteListTokenKey = redisRepository.findWhiteListTokenKey(memberId, jwtRefreshToken);
        if(whiteListTokenKey!=null) redisRepository.delete(whiteListTokenKey);
    }
}