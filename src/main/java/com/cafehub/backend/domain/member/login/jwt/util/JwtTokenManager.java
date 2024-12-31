package com.cafehub.backend.domain.member.login.jwt.util;

import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.jwt.dto.JwtPayloadCreateDTO;
import com.cafehub.backend.domain.member.repository.RedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cafehub.backend.common.constants.CafeHubConstants.JWT_ACCESS_TOKEN;
import static com.cafehub.backend.common.constants.CafeHubConstants.JWT_REFRESH_TOKEN;

@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenManager {

    private final JwtProvider jwtProvider;
    private final RedisRepository redisRepository;
    public Map<String, String> issueJwtTokens(Member member, String provider) {

        JwtPayloadCreateDTO payload = JwtPayloadCreateDTO.from(member.getId(),provider,member.getRole());

        String accessToken = jwtProvider.createJwtAccessToken(payload);
        String refreshToken = jwtProvider.createJwtRefreshToken(payload);

        redisRepository.save(member.getId(), refreshToken);

        log.info("JWT 발급 성공");
        
        return Map.of(JWT_ACCESS_TOKEN, accessToken, JWT_REFRESH_TOKEN, refreshToken);
    }
}
