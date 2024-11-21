package com.cafehub.backend.domain.member.mypage.dto.response;

import com.cafehub.backend.domain.member.entity.Member;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyPageResponseDTO {

    private String memberImgUrl;
    private String nickname;
    private String email;

    public static MyPageResponseDTO convertMemberToMyPageResponseDTO(Member member){

        return MyPageResponseDTO.builder().
                                 memberImgUrl(member.getProfileImg().getUrl()).
                                 nickname(member.getNickname()).
                                 email(member.getEmail()).
                                 build();
    }
}
