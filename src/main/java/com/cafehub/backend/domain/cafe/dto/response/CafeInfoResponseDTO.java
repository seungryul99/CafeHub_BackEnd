package com.cafehub.backend.domain.cafe.dto.response;


import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.entity.Theme;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
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

        @JsonFormat(pattern = "yy.MM.dd hh.mm")
        private LocalDateTime reviewCreateAt;

        private Integer likeCnt;

        private Integer commentCnt;

        // 객체를 생성 할 때  default == false가 유의미하게 활용됨
        private boolean likeChecked;

        private List<String> photoUrls;


        // TOP N 개의 리뷰를 가져오는 Repository의 메서드는 재사용 성이 없을 것이라고 판단해서 바로 DTO로 변환
        // likeChecked, photoUrls는 추후 해당 DTO에서 세팅 기본값은 false, null
        @QueryProjection
        public BestReviewDetail(Long reviewId, String author, Integer reviewRating, String reviewContent,
                                LocalDateTime reviewCreateAt, Integer likeCnt, Integer commentCnt) {

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




    // 여기서 수행하는게 맞을까? Converter로 옮길까? 정적 메서드로 제공해도 되는가? 고민
    public static CafeInfoResponseDTO of(Cafe cafe,
                                                         Boolean bookmarkChecked,
                                                         List<CafeInfoResponseDTO.BestMenuDetail> bestMenuList,
                                                         List<CafeInfoResponseDTO.BestReviewDetail> topNReview){

        return CafeInfoResponseDTO.builder()
                .cafeId(cafe.getId())
                .cafePhotoUrl(cafe.getCafeImg().getUrl())
                .cafeName(cafe.getName())
                .cafeTheme(cafe.getTheme())
                .cafeReviewCnt(cafe.getReviewCnt())
                .cafeOperationHour(cafe.getOperationHours())
                .cafeAddress(cafe.getAddress())
                .cafePhone(cafe.getPhone())
                .cafeRating(cafe.getRating())
                .bookmarkChecked(bookmarkChecked)
                .bestMenuList(bestMenuList)
                .bestReviewList(topNReview)
                .build();
    }


}