package com.cafehub.backend.domain.bookmark.entity;

import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Builder
    private Bookmark(Member member, Cafe cafe) {
        this.member = member;
        this.cafe = cafe;
    }
}