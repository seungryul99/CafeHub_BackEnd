package com.cafehub.backend.domain.member.mypage.repository;

import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
    boolean existsByNickname(String nickname);
}
