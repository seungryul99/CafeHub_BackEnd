package com.cafehub.backend.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AllReviewGetRequestDTO {

    private Long cafeId;

    private int currentPage;


    public static AllReviewGetRequestDTO of (Long cafeId, int currentPage){
        return AllReviewGetRequestDTO.builder()
                        .cafeId(cafeId)
                        .currentPage(currentPage)
                        .build();
    }
}
