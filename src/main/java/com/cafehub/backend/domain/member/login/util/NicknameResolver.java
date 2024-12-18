package com.cafehub.backend.domain.member.login.util;

import java.util.UUID;

import static com.cafehub.backend.common.constants.CafeHubConstants.MEMBER_NICKNAME_MAX_LENGTH;

public class NicknameResolver {

    public static String adjustNicknameLength (String nickname){

        if (nickname.length() > MEMBER_NICKNAME_MAX_LENGTH) nickname = nickname.substring(0,10);

        return nickname;
    }

    public static String adjustDuplicateNickname (String nickname){
        return nickname + UUID.randomUUID().toString().substring(0,3);
    }

}
