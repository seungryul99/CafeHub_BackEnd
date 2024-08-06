package com.cafehub.backend.member.entity;


import com.cafehub.backend.common.BaseEntity;
import com.cafehub.backend.common.Image;
import jakarta.persistence.*;
import lombok.*;


/**
 *   모든 엔티티의 생성 전략은 빌더로 통일, 생성자 사용 x, setter 사용 x
 */

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Embedded
    private Image profileImg;
}
