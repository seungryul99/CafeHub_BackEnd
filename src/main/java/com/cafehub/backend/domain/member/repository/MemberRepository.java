package com.cafehub.backend.domain.member.repository;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.jwt.JwtMemberPayloadDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByAuthInfo(AuthInfo authInfo);

    boolean existsByAuthInfo(AuthInfo authInfo);
}
