package com.cafehub.backend.domain.member.login.repository;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Member, Long> {

    Member findByAuthInfo(AuthInfo authInfo);
    boolean existsByNickname(String nickname);
}
