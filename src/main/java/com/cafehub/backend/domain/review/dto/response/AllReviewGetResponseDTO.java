package com.cafehub.backend.domain.review.dto.response;

import com.cafehub.backend.domain.review.dto.ReviewDetail;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllReviewGetResponseDTO {

    private List<ReviewDetail> reviewList;

    private Double cafeRating;

    private Integer cafeReviewCnt;

    private Boolean isLast;

    private Integer currentPage;



    public static AllReviewGetResponseDTO of(List<ReviewDetail> reviewList, Double cafeRating,
                                      Integer cafeReviewCnt, Boolean isLast, Integer currentPage){

        return AllReviewGetResponseDTO.builder()
                .reviewList(reviewList)
                .cafeRating(cafeRating)
                .cafeReviewCnt(cafeReviewCnt)
                .isLast(isLast)
                .currentPage(currentPage)
                .build();
    }
}
