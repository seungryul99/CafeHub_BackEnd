package com.cafehub.backend.domain.member.repository;

import com.cafehub.backend.domain.member.entity.Member;

public interface MemberRepositoryCustom {

    Member findMemberAndAuthInfoByAppId(Long appId);
}
