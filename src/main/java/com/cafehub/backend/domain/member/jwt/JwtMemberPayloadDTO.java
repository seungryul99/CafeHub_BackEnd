package com.cafehub.backend.domain.member.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtMemberPayloadDTO {

    private Long memberId;

    private String nickname;

}
