package com.cafehub.backend.domain.member.repository;

import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.jwt.MemberInfoDto;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByAppId(String appId);

    // jwt 발급을 위해서 바로 DTO레벨로 조회
    @Query("SELECT new com.cafehub.backend.domain.member.jwt.MemberInfoDto(m.id, m.nickname) FROM Member m WHERE m.appId = :appId")
    MemberInfoDto findByAppId(String appId);
}
