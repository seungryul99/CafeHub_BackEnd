package com.cafehub.backend.domain.review.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewCreateRequestDTO;
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
@RequestMapping("/api/auth")
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping(value = "/cafe/{cafeId}/review", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<ReviewCreateResponseDTO>> writeReview(@PathVariable Long cafeId,
                                                                            @RequestPart ("ReviewData") ReviewCreateRequestDTO requestDTO,
                                                                            @RequestPart (value = "photos", required = false) List<MultipartFile> reviewPhotos){

        requestDTO.setCafeId(cafeId);

        log.info("리뷰 정형 데이터 확인, CafeId : {}, 리뷰 내용 : {}, 별점 : {}",cafeId, requestDTO.getReviewContent(), requestDTO.getReviewRating());
        log.info("리뷰 비정형 데이터 확인, 사진 장 수 : {}", reviewPhotos==null ? 0 : reviewPhotos.size());



        return ResponseEntity.ok(reviewService.createReview(requestDTO, reviewPhotos));
    }
}
