package com.cafehub.backend.domain.review.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.common.util.S3KeyGenerator;
import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import com.cafehub.backend.domain.review.dto.request.ReviewCreateRequestDTO;
import com.cafehub.backend.domain.review.dto.response.ReviewCreateResponseDTO;
import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import com.cafehub.backend.domain.reviewPhoto.entity.ReviewPhoto;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3Bucket;

    private final S3Operations s3Operations;

    private final S3KeyGenerator s3KeyGenerator;

    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private final ReviewRepository reviewRepository;

    private final CafeRepository cafeRepository;

    private final MemberRepository memberRepository;


    public ResponseDTO<ReviewCreateResponseDTO> createReview(ReviewCreateRequestDTO requestDTO, List<MultipartFile> reviewPhotos) {

        Map<String, String> reviewPhotoMap = new HashMap<>();


        /**
         *  Server -> S3 업로드 시 성능 개선이 필요함
         */
        log.info("리뷰 사진 업로드");
        long startTime = System.currentTimeMillis();
        if(reviewPhotos!=null){
            // s3에 사진 업로드
            for (MultipartFile file : reviewPhotos){

                String key = s3KeyGenerator.generateReviewPhotoImgS3Key(file.getOriginalFilename());
                String url;
                try {
                    url = String.valueOf(s3Operations.upload(s3Bucket, key, file.getInputStream()).getURL());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                reviewPhotoMap.put(key,url);
            }
        }
        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        log.info("===== 리뷰 사진 업로드에 걸린시간 {}ms: ",endTime - startTime );

        /**
         *   성능 개선 테스트를 위해 우선 최악의 시나리오로 리뷰, 리뷰사진 등록을 진행함
         */

        Cafe cafe = cafeRepository.findById(requestDTO.getCafeId()).get();
        Member member = memberRepository.findById(jwtThreadLocalStorage.getMemberIdFromJwt()).get();


        Review review = Review.builder()
                .content(requestDTO.getReviewContent())
                .rating(requestDTO.getReviewRating())
                .likeCnt(0)
                .commentCnt(0)
                .writer(jwtThreadLocalStorage.getMemberNicknameFromJwt())
                .member(member)
                .cafe(cafe)
                .reviewPhotos(new ArrayList<>())
                .build();

        for (String key : reviewPhotoMap.keySet()) {

            ReviewPhoto reviewPhoto = ReviewPhoto.builder()
                    .reviewPhoto(new Image(key, reviewPhotoMap.get(key)))
                    .review(review)
                    .build();

            review.getReviewPhotos().add(reviewPhoto);
        }

        reviewRepository.save(review);

        return ResponseDTO.success(new ReviewCreateResponseDTO(review.getId()));
    }

}
