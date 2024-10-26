package com.cafehub.backend.domain.cafe.dto.response;


import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.entity.Theme;
import com.cafehub.backend.domain.review.dto.ReviewDetail;
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

    private List<ReviewDetail> bestReviewList;




    // 여기서 수행하는게 맞을까? Converter로 옮길까? 정적 메서드로 제공해도 되는가? 고민
    public static CafeInfoResponseDTO of(Cafe cafe, Boolean bookmarkChecked,
                                         List<CafeInfoResponseDTO.BestMenuDetail> bestMenuList,
                                         List<ReviewDetail> topNReview){

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