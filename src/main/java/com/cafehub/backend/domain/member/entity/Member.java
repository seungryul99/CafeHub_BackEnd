package com.cafehub.backend.domain.member.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.authInfo.entity.AuthInfo;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Embedded
    private Image profileImg;

    @OneToOne (fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "auth_info_id", unique = true)
    private AuthInfo authInfo;



    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfileImg(String key, String profileImg){
        this.profileImg.updateUrl(key, profileImg);
    }

}
