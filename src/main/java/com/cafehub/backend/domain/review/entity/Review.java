package com.cafehub.backend.domain.review.entity;


import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.reviewPhoto.entity.ReviewPhoto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Integer likeCnt;

    @Column(nullable = false)
    private Integer commentCnt;

    @Column(nullable = false)
    private String writer;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewPhoto> reviewPhotos = new ArrayList<>();

    @Builder
    private Review(String content, Integer rating, Integer likeCnt, Integer commentCnt,
                  String writer, Member member, Cafe cafe, List<ReviewPhoto> reviewPhotos) {

        this.content = content;
        this.rating = rating;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.writer = writer;
        this.member = member;
        this.cafe = cafe;
        this.reviewPhotos = reviewPhotos;
    }

    public void updateWriterByChangeNickname(String writer){
        this.writer = writer;
    }

    public void updateLikeCntByAddReviewLike(){
        likeCnt++;
    }

    public void updateLikeCntByDeleteReviewLike(){
        likeCnt--;
    }

    public void updateCommentCntByAddComment(){
        commentCnt++;
    }

    public void updateCommentCntByDeleteComment(){
        commentCnt--;
    }
}
