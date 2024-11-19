package com.cafehub.backend.domain.review.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDeleteRequestDTO {

    private Long reviewId;

    private Long cafeId;
}
