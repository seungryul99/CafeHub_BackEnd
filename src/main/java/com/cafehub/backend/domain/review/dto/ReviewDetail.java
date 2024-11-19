package com.cafehub.backend.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class ReviewDetail {

    private Long reviewId;
    private String author;
    private String authorProfile;
    private Integer reviewRating;
    private String reviewContent;

    @JsonFormat(pattern = "yy.MM.dd hh.mm")
    private LocalDateTime reviewCreateAt;

    private Integer likeCnt;
    private Integer commentCnt;

    // 아래 두 필드는 객체를 생성할 때 default 값인 false가 유의미 하게 사용됨.
    private boolean likeChecked;
    private boolean reviewManagement;
    private List<String> photoUrls;

    @QueryProjection
    public ReviewDetail(Long reviewId, String author, String authorProfile, Integer reviewRating, String reviewContent,
                            LocalDateTime reviewCreateAt, Integer likeCnt, Integer commentCnt) {

        this.reviewId = reviewId;
        this.author = author;
        this.authorProfile = authorProfile;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewCreateAt = reviewCreateAt;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
    }

    public void updateReviewManagementEnabled(){
        reviewManagement = true;
    }
}
