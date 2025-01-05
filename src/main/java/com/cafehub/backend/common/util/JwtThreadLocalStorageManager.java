package com.cafehub.backend.common.util;

import com.cafehub.backend.domain.member.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class JwtThreadLocalStorageManager {
    private static final ThreadLocal<MemberAuthentication> memberAuthenticationHolder = new ThreadLocal<>();

    public void setMemberAuthentication(MemberAuthentication authentication){
        memberAuthenticationHolder.set(authentication);
    }

    public void clear(){
        memberAuthenticationHolder.remove();
    }

    public Long getMemberId(){
        return memberAuthenticationHolder.get().getMemberId();
    }

    public Role getMemberRole(){
        return memberAuthenticationHolder.get().getMemberRole();
    }

    public boolean isLoginMember(){
        return memberAuthenticationHolder.get() != null;
    }

    public String getProvider(){
        return memberAuthenticationHolder.get().getProvider();
    }
}