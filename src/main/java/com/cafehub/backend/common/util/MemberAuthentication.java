package com.cafehub.backend.common.util;

import com.cafehub.backend.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberAuthentication {
    private final Long memberId;
    private final Role memberRole;
    private final String provider;
}
