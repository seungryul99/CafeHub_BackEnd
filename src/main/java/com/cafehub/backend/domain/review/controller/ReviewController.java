package com.cafehub.backend.domain.review.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.review.dto.request.AllReviewGetRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewCreateRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewDeleteRequestDTO;
import com.cafehub.backend.domain.review.dto.response.AllReviewGetResponseDTO;
import com.cafehub.backend.domain.review.dto.response.ReviewCreateResponseDTO;
import com.cafehub.backend.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping(value = "/auth/cafe/{cafeId}/review", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<ReviewCreateResponseDTO>> writeReview(@PathVariable Long cafeId,
                                                                            @RequestPart ("ReviewData") ReviewCreateRequestDTO requestDTO,
                                                                            @RequestPart (value = "photos", required = false) List<MultipartFile> reviewPhotos){

        requestDTO.setCafeId(cafeId);

        log.info("리뷰 정형 데이터 확인, CafeId : {}, 리뷰 내용 : {}, 별점 : {}",cafeId, requestDTO.getReviewContent(), requestDTO.getReviewRating());
        log.info("리뷰 비정형 데이터 확인, 사진 장 수 : {}", reviewPhotos==null ? 0 : reviewPhotos.size());

        return ResponseEntity.ok(reviewService.createReview(requestDTO, reviewPhotos));
    }



    @GetMapping("/optional-auth/cafe/{cafeId}/reviews/{currentPage}")
    public ResponseEntity<ResponseDTO<AllReviewGetResponseDTO>> readAllReview(@PathVariable Long cafeId,
                                                                              @PathVariable int currentPage){

        return ResponseEntity.ok(reviewService.getAllReview(new AllReviewGetRequestDTO(cafeId, currentPage)));
    }


    @DeleteMapping("/auth/review")
    public ResponseEntity<ResponseDTO<Void>> deleteReview(@RequestBody ReviewDeleteRequestDTO requestDTO){

        return ResponseEntity.ok(reviewService.deleteReview(requestDTO));
    }

}
