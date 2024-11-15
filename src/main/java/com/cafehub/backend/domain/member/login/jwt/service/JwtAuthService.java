package com.cafehub.backend.domain.member.login.jwt.service;

import com.cafehub.backend.domain.member.login.jwt.dto.JwtTokenPayloadCreateDTO;
import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import com.cafehub.backend.domain.member.login.jwt.util.JwtProvider;
import com.cafehub.backend.domain.member.login.jwt.util.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService {

    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;
    private final JwtProvider jwtProvider;

    public String reIssueJwtAccessTokenWithRefreshToken(String jwtRefreshToken) {

        // 전달 받은 jwt Refresh Token 검증
        jwtValidator.validateJwtRefreshToken(jwtRefreshToken);

        // Refresh Token으로 부터 Access Token 재발급을 위한 정보를 추출함
        JwtTokenPayloadCreateDTO payload = JwtTokenPayloadCreateDTO.builder()
                .memberId(jwtPayloadReader.getMemberId(jwtRefreshToken))
                .provider(jwtPayloadReader.getProvider(jwtRefreshToken))
                .build();


        return jwtProvider.createJwtAccessToken(payload);
    }
}
