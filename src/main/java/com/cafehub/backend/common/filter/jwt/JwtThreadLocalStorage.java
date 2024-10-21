package com.cafehub.backend.common.filter.jwt;

import com.cafehub.backend.domain.member.login.jwt.JwtPayloadReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtThreadLocalStorage {

    private final JwtPayloadReader jwtPayloadReader;

    private final ThreadLocal<String> jwtAccessTokenHolder = new ThreadLocal<>();

    void initJwtAccessTokenHolder(String jwtAccessToken){
        jwtAccessTokenHolder.set(jwtAccessToken);
    }


    void clearJwtAccessTokenHolder(){
        jwtAccessTokenHolder.remove();
    }


    public boolean isLoginMember(){
        return jwtAccessTokenHolder.get() != null;
    }


    public Long getMemberIdFromJwt(){
        return jwtPayloadReader.getMemberId(jwtAccessTokenHolder.get());
    }

    public String getMemberNicknameFromJwt(){
        return jwtPayloadReader.getNickname(jwtAccessTokenHolder.get());
    }


}