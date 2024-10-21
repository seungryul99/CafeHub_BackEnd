package com.cafehub.backend.domain.member.mypage.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyPageResponseDTO {

    private String memberImgUrl;

    private String nickname;

    private String email;
}
