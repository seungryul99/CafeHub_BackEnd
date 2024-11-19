package com.cafehub.backend.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLikeRequestDTO {

    private Long reviewId;

    private Boolean reviewLike;

}
