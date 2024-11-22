package com.cafehub.backend.domain.review.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDeleteRequestDTO {

    @NotNull(message = "reviewId는 반드시 필요합니다")
    @Positive (message = "reviewId는 1이상의 정수여야 합니다")
    private Long reviewId;

    @NotNull (message = "cafeId는 반드시 필요합니다")
    @Positive (message = "cafeId는 1이상의 정수여야 합니다")
    private Long cafeId;
}
