package com.cafehub.backend.domain.review.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.common.exception.global.CommonErrorCode;
import com.cafehub.backend.domain.member.mypage.exception.InvalidMemberProfileImgExtensionException;
import com.cafehub.backend.domain.member.mypage.exception.TooLongMemberProfileImgNameException;
import com.cafehub.backend.domain.review.dto.request.AllReviewGetRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewCreateRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewDeleteRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewLikeRequestDTO;
import com.cafehub.backend.domain.review.dto.response.AllReviewGetResponseDTO;
import com.cafehub.backend.domain.review.dto.response.ReviewCreateResponseDTO;
import com.cafehub.backend.domain.review.exception.InvalidReviewPhotoExtensionException;
import com.cafehub.backend.domain.review.exception.TooLongReviewPhotoNameException;
import com.cafehub.backend.domain.review.exception.TooManyReviewPhotosRequestException;
import com.cafehub.backend.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController implements ReviewControllerAPI{

    private final ReviewService reviewService;


    @PostMapping(value = "/auth/cafe/{cafeId}/review", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<ReviewCreateResponseDTO>> writeReview(@PathVariable Long cafeId,
                                                                            @RequestPart ("ReviewData") @Validated ReviewCreateRequestDTO requestDTO,
                                                                            @RequestPart (value = "photos", required = false) List<MultipartFile> reviewPhotos){


        /**
         * 리뷰 사진들에 대한 검증 로직은 추후 커스텀 어노테이션으로 빼버릴 예정
         */

        if (reviewPhotos != null) {
            if (reviewPhotos.size() > 5) throw new TooManyReviewPhotosRequestException();

            for (MultipartFile file : reviewPhotos) {
                String orginalFilename = file.getOriginalFilename();

                if (orginalFilename.length() > 50) throw new TooLongReviewPhotoNameException();

                // 확장자 추출
                int dotIndex = orginalFilename != null ? orginalFilename.lastIndexOf('.') : -1;
                if (dotIndex == -1 || !List.of("png", "jpg", "jpeg").contains(orginalFilename.substring(dotIndex + 1).toLowerCase()))
                    throw new InvalidReviewPhotoExtensionException();
            }
        }

        // 어노테이션으로 만들 예정, 임시 예외 처리
        if(requestDTO.getReviewContent().trim().length() < 5) throw new CafeHubException(CommonErrorCode._BAD_REQUEST);


        log.info("리뷰 정형 데이터 확인, CafeId : {}, 리뷰 내용 : {}, 별점 : {}",cafeId, requestDTO.getReviewContent(), requestDTO.getReviewRating());
        log.info("리뷰 비정형 데이터 확인, 사진 장 수 : {}", reviewPhotos==null ? 0 : reviewPhotos.size());

        return ResponseEntity.ok(reviewService.createReview(cafeId, requestDTO, reviewPhotos));
    }



    @GetMapping("/optional-auth/cafe/{cafeId}/reviews/{currentPage}")
    public ResponseEntity<ResponseDTO<AllReviewGetResponseDTO>> readAllReview(@PathVariable Long cafeId,
                                                                              @PathVariable int currentPage){

        return ResponseEntity.ok(reviewService.getAllReview(AllReviewGetRequestDTO.of(cafeId, currentPage)));
    }


    @DeleteMapping("/auth/review")
    public ResponseEntity<ResponseDTO<Void>> deleteReview(@RequestBody @Validated ReviewDeleteRequestDTO requestDTO){

        return ResponseEntity.ok(reviewService.deleteReview(requestDTO));
    }


    @PostMapping(value = "/auth/review/like")
    public ResponseEntity<ResponseDTO<Void>> reviewLikeManage(@RequestBody @Validated ReviewLikeRequestDTO requestDTO){

        if(requestDTO.getReviewLike()) return ResponseEntity.ok(reviewService.reviewLike(requestDTO));
        else return ResponseEntity.ok(reviewService.cancelReviewLike(requestDTO));
    }

}
