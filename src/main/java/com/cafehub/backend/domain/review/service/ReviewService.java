package com.cafehub.backend.domain.review.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.JwtThreadLocalStorageManager;
import com.cafehub.backend.common.legacy.JwtThreadLocalStorage;
import com.cafehub.backend.common.util.S3KeyGenerator;
import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.exception.CafeNotFoundException;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.comment.entity.Comment;
import com.cafehub.backend.domain.comment.repository.CommentRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import com.cafehub.backend.domain.review.dto.ReviewDetail;
import com.cafehub.backend.domain.review.dto.request.AllReviewGetRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewCreateRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewDeleteRequestDTO;
import com.cafehub.backend.domain.review.dto.request.ReviewLikeRequestDTO;
import com.cafehub.backend.domain.review.dto.response.AllReviewGetResponseDTO;
import com.cafehub.backend.domain.review.dto.response.ReviewCreateResponseDTO;
import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.review.exception.ReviewNotFoundException;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import com.cafehub.backend.domain.reviewLike.entity.ReviewLike;
import com.cafehub.backend.domain.reviewLike.exception.ReviewLikeAlreadyExistException;
import com.cafehub.backend.domain.reviewLike.exception.ReviewLikeNotFoundException;
import com.cafehub.backend.domain.reviewLike.repository.ReviewLikeRepository;
import com.cafehub.backend.domain.reviewPhoto.dto.ReviewPhotoDetail;
import com.cafehub.backend.domain.reviewPhoto.entity.ReviewPhoto;
import com.cafehub.backend.domain.reviewPhoto.repository.ReviewPhotoRepository;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Slice;
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

    private final JwtThreadLocalStorageManager threadLocalStorageManager;

    private final ReviewRepository reviewRepository;

    private final CafeRepository cafeRepository;

    private final MemberRepository memberRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final ReviewPhotoRepository reviewPhotoRepository;

    private final CommentRepository commentRepository;

    public ResponseDTO<ReviewCreateResponseDTO> createReview(Long cafeId, ReviewCreateRequestDTO requestDTO, List<MultipartFile> reviewPhotos) {

        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(CafeNotFoundException::new);
        Member member = memberRepository.findById(threadLocalStorageManager.getMemberId()).orElseThrow(MemberNotFoundException::new);


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




        Review review = Review.builder()
                .content(requestDTO.getReviewContent())
                .rating(requestDTO.getReviewRating())
                .likeCnt(0)
                .commentCnt(0)
                .writer(member.getNickname())
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

        cafe.updateRatingAndReviewCountByAddReview(review.getRating());

        return ResponseDTO.success(new ReviewCreateResponseDTO(review.getId()));
    }

    public ResponseDTO<AllReviewGetResponseDTO> getAllReview(AllReviewGetRequestDTO requestDTO) {

        Cafe cafe = cafeRepository.findById(requestDTO.getCafeId()).orElseThrow(CafeNotFoundException::new);
        Slice<ReviewDetail> reviewDetails = reviewRepository.findReviewsBySlice(requestDTO);


        if (threadLocalStorageManager.isLoginMember()){

            Member loginMember = memberRepository.findById(threadLocalStorageManager.getMemberId()).orElseThrow(MemberNotFoundException::new);
            String loginMemberNickname = loginMember.getNickname();

            for (ReviewDetail rd : reviewDetails.getContent()){

                if (rd.getAuthor().equals(loginMemberNickname)) rd.updateReviewManagementEnabled();
            }

            updateLikeStatusInReviews(reviewDetails.getContent(), threadLocalStorageManager.getMemberId());
        }
        updateReviewPhotoUrls(reviewDetails.getContent());

        return ResponseDTO.success(AllReviewGetResponseDTO.of(reviewDetails.getContent(), cafe.getRating(), cafe.getReviewCnt(), reviewDetails.isLast(), requestDTO.getCurrentPage()));
    }


    private void updateLikeStatusInReviews(List<ReviewDetail> reviewDetails, Long loginMemberId) {

        // Top N 리뷰의 ID 리스트 만들기
        List<Long> reviewIds = reviewDetails.stream()
                .map(ReviewDetail::getReviewId)
                .toList();

        // 한 번의 DB 커넥션으로 사용자가 좋아요를 누른 리뷰 아이디 들을 검색
        List<Long> likeCheckedReviewIds =  reviewLikeRepository.findLikeCheckedReviewIds(loginMemberId, reviewIds);

        // 최대 N개의 리뷰들을 순회하게 되지만 N이 현재는 2이고 추후 아무리 높아져도 5 정도일 것으로 예상해서 그냥 전수 순회 로직을 짬
        for (ReviewDetail rd : reviewDetails){

            if(likeCheckedReviewIds.contains(rd.getReviewId())){
                rd.setLikeChecked(true);
            }
        }
    }

    private void updateReviewPhotoUrls(List<ReviewDetail> reviews){

        // Top N 리뷰의 ID 리스트 만들기
        List<Long> reviewIds = reviews.stream()
                .map(ReviewDetail::getReviewId)
                .toList();

        // 한 번의 DB 커넥션으로 모든 사진 조회
        List<ReviewPhotoDetail> photoUrls = reviewPhotoRepository.findAllByReviewIdsIn(reviewIds);

        // 리뷰 ID에 따라 사진 URL을 매핑하는 맵 생성 후 세팅, 리뷰 Id : photoUrls 의 레퍼런스 구조
        Map<Long, List<String>> reviewPhotosMap = new HashMap<>();

        for (ReviewDetail rd : reviews){
            reviewPhotosMap.put(rd.getReviewId(),new ArrayList<>());
        }

        for (ReviewPhotoDetail rp : photoUrls){
            reviewPhotosMap.get(rp.getReviewId()).add(rp.getPhotoUrl());
        }

        for (ReviewDetail x : reviews) {
            x.setPhotoUrls(reviewPhotosMap.get(x.getReviewId()));
        }
    }


    /**
     *   리뷰 삭제를 위해서 처리 해야 할 일들
     *
     *   1. 리뷰 삭제로 인한 카페의 리뷰수 조정, 별점 조정
     *   2. 삭제할 리뷰의 모든 좋아요 삭제
     *   3. 삭제할 리뷰의 모든 리뷰 사진들 삭제
     *   4. 삭제할 리뷰의 s3 상 모든 사진 삭제
     *   5. 삭제할 리뷰의 모든 댓글 삭제
     */
    public ResponseDTO<Void> deleteReview(ReviewDeleteRequestDTO requestDTO) {

        Cafe cafe = cafeRepository.findById(requestDTO.getCafeId()).orElseThrow(CafeNotFoundException::new);
        Review deleteReview = reviewRepository.findById(requestDTO.getReviewId()).orElseThrow(ReviewNotFoundException::new);
        List<Comment> commentList = commentRepository.findAllByReviewId(requestDTO.getReviewId());

        cafe.updateRatingAndReviewCountByDeleteReview(deleteReview.getRating());


        reviewLikeRepository.deleteAllByReviewId(requestDTO.getReviewId());

        List<ReviewPhoto> deleteReviewReviewPhotos = deleteReview.getReviewPhotos();

        for(ReviewPhoto rp : deleteReviewReviewPhotos){

            s3Operations.deleteObject(s3Bucket, rp.getReviewPhoto().getKey());
        }

        commentRepository.deleteAll(commentList);

        reviewRepository.deleteById(requestDTO.getReviewId());


        return ResponseDTO.success(null);
    }

    public ResponseDTO<Void> reviewLike(ReviewLikeRequestDTO requestDTO) {

        Review review = reviewRepository.findById(requestDTO.getReviewId()).orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(threadLocalStorageManager.getMemberId()).orElseThrow(MemberNotFoundException::new);

        if(reviewLikeRepository.existsByReviewIdAndMemberId(review.getId(), member.getId())) throw new ReviewLikeAlreadyExistException();

        ReviewLike reviewLike = ReviewLike.builder()
                .review(review)
                .member(member)
                .build();

        reviewLikeRepository.save(reviewLike);

        review.updateLikeCntByAddReviewLike();

        return ResponseDTO.success(null);
    }

    public ResponseDTO<Void> cancelReviewLike(ReviewLikeRequestDTO requestDTO) {


        Review review = reviewRepository.findById(requestDTO.getReviewId()).orElseThrow(ReviewNotFoundException::new);

        if(!reviewLikeRepository.existsByReviewIdAndMemberId(review.getId(), threadLocalStorageManager.getMemberId())) throw new ReviewLikeNotFoundException();

        reviewLikeRepository.deleteByReviewIdAndMemberId(requestDTO.getReviewId(), threadLocalStorageManager.getMemberId());

        review.updateLikeCntByDeleteReviewLike();

        return ResponseDTO.success(null);
    }
}
