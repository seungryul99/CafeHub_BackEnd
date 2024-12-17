package com.cafehub.backend.domain.member.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import com.cafehub.backend.domain.member.login.dto.response.KakaoUserResourceResponseDTO;
import com.cafehub.backend.domain.member.login.dto.response.OAuthUserResourceResponseDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.cafehub.backend.common.constants.CafeHubConstants.MEMBER_PROFILE_DEFAULT_IMAGE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Embedded
    private Image profileImg;

    @OneToOne (fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "auth_info_id", unique = true)
    private AuthInfo authInfo;


    @Builder
    private Member(String nickname, String email, Image profileImg, AuthInfo authInfo) {
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.authInfo = authInfo;
    }


    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfileImg(String key, String profileImg){
        this.profileImg.updateUrl(key, profileImg);
    }

    public static Member from(AuthInfo authInfo, String nickname, String email, String profileImgUrl){

        return Member.builder()
                .email(email)
                .nickname(nickname)
                .profileImg(profileImgUrl != null ? new Image(profileImgUrl) : new Image(MEMBER_PROFILE_DEFAULT_IMAGE))
                .authInfo(authInfo)
                .build();
    }
}
