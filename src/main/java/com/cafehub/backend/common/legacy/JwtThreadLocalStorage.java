package com.cafehub.backend.common.legacy;

import com.cafehub.backend.domain.member.login.jwt.util.JwtPayloadReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtThreadLocalStorage {

    // [FeedBack] Thread Local을 관리하는 애를 하나 더 만들면 좋지 않을까?
    private final JwtPayloadReader jwtPayloadReader;
    private final ThreadLocal<String> jwtAccessTokenHolder = new ThreadLocal<>();

    public void initJwtAccessTokenHolder(String jwtAccessToken){
        jwtAccessTokenHolder.set(jwtAccessToken);
    }

    public void clearJwtAccessTokenHolder(){
        jwtAccessTokenHolder.remove();
    }

    public boolean isLoginMember(){
        return jwtAccessTokenHolder.get() != null;
    }

    public Long getMemberIdFromJwt(){
        return jwtPayloadReader.getMemberId(jwtAccessTokenHolder.get());
    }

    public String getOAuthProviderNameFromJwt(){
        return jwtPayloadReader.getProvider(jwtAccessTokenHolder.get());
    }

}