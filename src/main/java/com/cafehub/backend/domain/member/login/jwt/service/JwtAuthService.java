package com.cafehub.backend.domain.member.login.jwt.service;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.authInfo.repository.AuthInfoRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenBlockedException;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtPayloadCreateDTO;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import com.cafehub.backend.domain.member.login.jwt.util.JwtProvider;
import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.JWT_ACCESS_TOKEN;
import static com.cafehub.backend.common.constants.CafeHubConstants.JWT_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtAuthService {

    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final AuthInfoRepository authInfoRepository;

    public Map<String, String> reIssueJwtAccessTokenWithRefreshToken(String jwtRefreshToken) {

        // 전달 받은 jwt Refresh Token 검증
        jwtValidator.validateJwtRefreshToken(jwtRefreshToken);
        Member member = memberRepository.findById(jwtPayloadReader.getMemberId(jwtRefreshToken)).orElseThrow(MemberNotFoundException::new);

        System.out.println("체크지점 1");
        
        // 프록시 지연로딩 설정된 부분 , 즉시로딩으로 DB 커넥션 줄일 수 있는 부분
        AuthInfo memberAuthinfo = member.getAuthInfo();

        // 블랙리스트 체크, 실제로 DB 커넥션이 발생하는 부분
        if(!memberAuthinfo.getJwtRefreshToken().equals(jwtRefreshToken)) throw new JwtRefreshTokenBlockedException();
        System.out.println("체크지점 2");


        // 전달 받은 Refresh Token으로 부터 Token 재발급을 위한 정보를 추출함
        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.from(jwtPayloadReader.getMemberId(jwtRefreshToken),jwtPayloadReader.getProvider(jwtRefreshToken));
//        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.builder()
//                .memberId(jwtPayloadReader.getMemberId(jwtRefreshToken))
//                .provider(jwtPayloadReader.getProvider(jwtRefreshToken))
//                .build();


        // 토큰 두 개 발급
        String reIssueAccess = jwtProvider.createJwtAccessToken(payload);
        String reIssueRefresh = jwtProvider.createJwtRefreshToken(payload);


        // 파라미터로 전송받은 jwtRefreshToken이 탈취 당할 경우 RefreshToken Rotate를 진행했지만 Refresh Token을 만료시켜 버린게 아니기 때문에
        // 리프레시 토큰이 2개로 늘어난 문제가 발생함. 어디선가 탈취 당할 경우 리프레시 토큰이 두개가 됨.

        // 변경감지 작동을 안함, 체크 포인트
        memberAuthinfo.updateJwtRefreshTokenByReIssue(reIssueRefresh, jwtPayloadReader.getExpiration(reIssueRefresh));
        authInfoRepository.save(memberAuthinfo);

        return  Map.of(
                JWT_ACCESS_TOKEN, reIssueAccess,
                JWT_REFRESH_TOKEN, reIssueRefresh
        );
    }
}
