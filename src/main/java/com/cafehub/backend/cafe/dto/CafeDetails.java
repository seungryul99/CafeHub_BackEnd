package com.cafehub.backend.cafe.dto;

import com.cafehub.backend.cafe.entity.Theme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CafeDetails {


    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private Theme cafeTheme;

    /**
     * BigDecimal : 정밀도가 중요하고 소수점 이하 자리가 필요한 경우, 메모리를 많이 쓰고 연산이 느림
     * double     : 약간의 부동소수점 오차가 허용되고, 성능이 중요한 경우
     * float      : 아주 낮은 정밀도가 필요하고, 메모리 사용량을 최소화하려는 경우
     */

    private Double cafeRating;

    private Integer cafeReviewNum;



    @QueryProjection
    public CafeDetails(Long cafeId, String cafePhotoUrl, String cafeName, Theme cafeTheme, Double cafeRating, Integer cafeReviewNum) {
        this.cafeId = cafeId;
        this.cafePhotoUrl = cafePhotoUrl;
        this.cafeName = cafeName;
        this.cafeTheme = cafeTheme;
        this.cafeRating = cafeRating;
        this.cafeReviewNum = cafeReviewNum;
    }
}
