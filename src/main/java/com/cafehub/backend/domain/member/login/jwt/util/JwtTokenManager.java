package com.cafehub.backend.domain.member.login.jwt.util;

import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtPayloadCreateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.*;

@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenManager {

    private final JwtProvider jwtProvider;
    private final JwtPayloadReader jwtPayloadReader;

    public Map<String, String> issueJwtTokens(Member member, String provider) {

        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.from(member.getId(),provider,member.getRole());

        String jwtAccessToken = jwtProvider.createJwtAccessToken(payload);
        String jwtRefreshToken = jwtProvider.createJwtRefreshToken(payload);

        member.getAuthInfo().updateAuthInfoByJwtIssue(jwtRefreshToken, jwtPayloadReader.getExpiration(jwtRefreshToken));

        log.info("JWT 발급 성공");
        
        return Map.of(
                JWT_ACCESS_TOKEN, jwtAccessToken,
                JWT_REFRESH_TOKEN, jwtRefreshToken
        );
    }
}
