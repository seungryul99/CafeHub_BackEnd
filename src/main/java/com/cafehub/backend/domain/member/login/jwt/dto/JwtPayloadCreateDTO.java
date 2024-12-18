package com.cafehub.backend.domain.member.login.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtPayloadCreateDTO {

    private final Long memberId;
    private final String provider;

    @Builder(access = AccessLevel.PRIVATE)
    private JwtPayloadCreateDTO(Long memberId, String provider){
        this.memberId = memberId;
        this.provider = provider;
    }

    public static JwtPayloadCreateDTO from(Long memberId, String provider){

        return JwtPayloadCreateDTO.builder()
                .memberId(memberId)
                .provider(provider)
                .build();
    }
}