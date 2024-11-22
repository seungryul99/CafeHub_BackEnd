package com.cafehub.backend.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateRequestDTO {

    @NotNull(message = "리뷰 별점은 반드시 필요합니다")
    @Min(value = 1, message = "리뷰 별점은 1보다 낮을 수 없습니다")
    @Max(value = 5, message = "리뷰 별점은 5보다 높을 수 없습니다")
    private Integer reviewRating;

    @NotBlank (message = "리뷰 내용은 공백으로만 이루어 질 수 없습니다")
    @Length (min = 5, max = 1000, message = "리뷰 내용은 최소 5자, 최대 1000자 까지 허용 됩니다")
    private String reviewContent;
}
