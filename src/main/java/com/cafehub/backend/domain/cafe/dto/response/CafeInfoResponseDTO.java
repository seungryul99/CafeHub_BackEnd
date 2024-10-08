package com.cafehub.backend.domain.cafe.dto.response;


import com.cafehub.backend.domain.cafe.entity.Theme;
import com.cafehub.backend.domain.reviewPhotos.dto.PhotoUrlResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CafeInfoResponseDTO {


    @Getter
    @Setter
    public static class BestMenuDetail{

        private Long menuId;

        private String name;

        private Integer price;


        @Builder
        @QueryProjection
        public BestMenuDetail(Long menuId, String name, Integer price) {
            this.menuId = menuId;
            this.name = name;
            this.price = price;
        }
    }


    @Getter
    @Setter
    public static class BestReviewDetail{

        private Long reviewId;

        private String author;

        private Integer reviewRating;

        private String reviewContent;

        private String reviewCreateAt;

        private Integer likeCnt;

        private Integer commentCnt;

        private Boolean likeChecked;

        private List<PhotoUrlResponseDTO> photoUrls;

        @Builder
        public BestReviewDetail(Long reviewId, String author, Integer reviewRating, String reviewContent, String reviewCreateAt, Integer likeCnt, Integer commentCnt, Boolean likeChecked, List<PhotoUrlResponseDTO> photoUrls) {
            this.reviewId = reviewId;
            this.author = author;
            this.reviewRating = reviewRating;
            this.reviewContent = reviewContent;
            this.reviewCreateAt = reviewCreateAt;
            this.likeCnt = likeCnt;
            this.commentCnt = commentCnt;
            this.likeChecked = likeChecked;
            this.photoUrls = photoUrls;
        }

        @QueryProjection
        public BestReviewDetail(Long reviewId, String author, Integer reviewRating, String reviewContent, String reviewCreateAt, Integer likeCnt, Integer commentCnt) {
            this.reviewId = reviewId;
            this.author = author;
            this.reviewRating = reviewRating;
            this.reviewContent = reviewContent;
            this.reviewCreateAt = reviewCreateAt;
            this.likeCnt = likeCnt;
            this.commentCnt = commentCnt;
        }

    }


    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private Theme cafeTheme;

    private Integer cafeReviewCnt;

    private String cafeOperationHour;

    private String cafeAddress;

    private String cafePhone;

    private Double cafeRating;

    private Boolean bookmarkChecked;

    private List<BestMenuDetail> bestMenuList;

    private List<BestReviewDetail> bestReviewList;
}