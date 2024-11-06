package com.cafehub.backend.domain.cafe.dto;

import com.cafehub.backend.domain.cafe.entity.Theme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeDetails {

    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private Theme cafeTheme;

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