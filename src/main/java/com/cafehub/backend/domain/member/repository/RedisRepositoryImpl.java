package com.cafehub.backend.domain.member.repository;

import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository{

    private final JwtPayloadReader jwtPayloadReader;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(Long memberId, String refreshToken) {

        String memberKey = "members:" + memberId;

        // idx 필드를 관리하고, 새 토큰을 저장
        Long idx = redisTemplate.opsForHash().increment(memberKey, "idx", 1); // idx 값을 증가시킴

        // 고유한 토큰 키
        String tokenKey = memberKey + ":token:" + idx;

        // ttl 계산
        long ttl = (jwtPayloadReader.getExpiration(refreshToken).getTime() - System.currentTimeMillis() + 100)/1000;

        redisTemplate.opsForValue().set(tokenKey,refreshToken,ttl, TimeUnit.SECONDS);
    }
}
