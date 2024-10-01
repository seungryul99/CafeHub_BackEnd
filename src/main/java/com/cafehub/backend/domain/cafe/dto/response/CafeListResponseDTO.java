package com.cafehub.backend.domain.cafe.dto.response;

import com.cafehub.backend.domain.cafe.entity.Theme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CafeListResponseDTO {

    @Getter
    @Setter
    @Builder
    public static class CafeDetails {

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



    private List<CafeDetails> cafeList;

    private Boolean isLast;

    private Integer currentPage;
}
