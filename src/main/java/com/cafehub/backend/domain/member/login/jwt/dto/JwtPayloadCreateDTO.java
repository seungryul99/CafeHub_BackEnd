package com.cafehub.backend.domain.member.login.jwt.dto;

import com.cafehub.backend.domain.member.entity.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtPayloadCreateDTO {

    private final Long memberId;
    private final String provider;
    private final Role role;

    @Builder(access = AccessLevel.PRIVATE)
    private JwtPayloadCreateDTO(Long memberId, String provider, Role role){
        this.memberId = memberId;
        this.provider = provider;
        this.role = role;
    }

    public static JwtPayloadCreateDTO from(Long memberId, String provider, Role role){

        return JwtPayloadCreateDTO.builder()
                .memberId(memberId)
                .provider(provider)
                .role(role)
                .build();
    }
}