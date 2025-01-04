package com.cafehub.backend.domain.member.login.jwt.service;

import com.cafehub.backend.common.util.JwtThreadLocalStorageManager;
import com.cafehub.backend.domain.member.login.exception.JwtRefreshTokenBlockedException;
import com.cafehub.backend.domain.member.login.jwt.util.JwtTokenManager;
import com.cafehub.backend.domain.member.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtReIssueService {

    private final JwtTokenManager jwtTokenManager;
    private final JwtThreadLocalStorageManager jwtThreadLocalStorageManager;
    private final RedisRepository redisRepository;
    public Map<String, String> reIssueJwt(String jwtRefreshToken) {

        Long memberId = jwtThreadLocalStorageManager.getMemberId();

        // 화이트리스트에 토큰이 없다면 차단 처리
        String whiteListTokenKey = redisRepository.findWhiteListTokenKey(memberId, jwtRefreshToken);
        if(whiteListTokenKey==null) throw new JwtRefreshTokenBlockedException();

        return jwtTokenManager.reIssueJwtTokens(whiteListTokenKey);
    }
}
