package com.cafehub.backend.domain.review.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewCreateRequestDTO;
import com.cafehub.backend.domain.review.dto.response.AllReviewGetResponseDTO;
import com.cafehub.backend.domain.review.dto.response.ReviewCreateResponseDTO;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
public interface ReviewControllerAPI {


    @PostMapping(value = "/auth/cafe/{cafeId}/review", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<ReviewCreateResponseDTO>> writeReview(@Positive(message = "리뷰를 작성 하려는 카페의 ID는 1이상의 정수 여야 합니다") Long cafeId,
                                                                            ReviewCreateRequestDTO requestDTO,
                                                                            List<MultipartFile> reviewPhotos);

    @GetMapping("/optional-auth/cafe/{cafeId}/reviews/{currentPage}")
    public ResponseEntity<ResponseDTO<AllReviewGetResponseDTO>> readAllReview(@Positive(message = "리뷰 리스트를 조회 하려는 카페의 ID는 1이상의 정수여야 합니다") Long cafeId,
                                                                              @PositiveOrZero(message = "리뷰 리스트 검색 조건중 현재 페이지는 0 이상의 정수만 입력해야 합니다") int currentPage);
}
