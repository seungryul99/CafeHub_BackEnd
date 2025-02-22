package com.cafehub.backend.domain.reviewLike.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    private ReviewLike(Member member, Review review) {
        this.member = member;
        this.review = review;
    }
}
