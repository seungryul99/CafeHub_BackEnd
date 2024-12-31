package com.cafehub.backend.domain.member.repository;

import com.cafehub.backend.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.cafehub.backend.domain.authInfo.entity.QAuthInfo.authInfo;
import static com.cafehub.backend.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Member findMemberAndAuthInfoByAppId(Long appId) {

        return jpaQueryFactory
                .selectFrom(member)
                .join(member.authInfo, authInfo)
                .fetchJoin()
                .where(authInfo.appId.eq(appId))
                .fetchOne();
    }
}
