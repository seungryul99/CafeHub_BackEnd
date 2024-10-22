package com.cafehub.backend.domain.member.login.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenPayloadCreateDTO {

    private Long memberId;

    private String nickname;

    private String provider;

}
