package com.cafehub.backend.domain.comment.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;


    @Builder
    private Comment(String content, String writer, Member member, Review review) {
        this.content = content;
        this.writer = writer;
        this.member = member;
        this.review = review;
    }

    public void updateWriterByChangeNickname(String writer) {

        this.writer = writer;
    }
}
