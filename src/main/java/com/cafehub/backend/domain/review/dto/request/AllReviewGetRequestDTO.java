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
}
