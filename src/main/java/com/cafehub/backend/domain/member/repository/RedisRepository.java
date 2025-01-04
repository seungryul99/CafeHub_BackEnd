package com.cafehub.backend.domain.member.repository;

import java.util.Map;

public interface RedisRepository {
    void save(Long memberId, String refreshToken);

    String findWhiteListTokenKey(Long memberId, String jwtRefreshToken);

    void delete(String key);
}
