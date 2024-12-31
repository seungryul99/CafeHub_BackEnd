package com.cafehub.backend.domain.member.repository;

public interface RedisRepository {
    void save(Long memberId, String refreshToken);
}
