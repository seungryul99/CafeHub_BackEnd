package com.cafehub.backend.domain.cafe.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.bookmark.repository.BookmarkRepository;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.exception.CafeNotFoundException;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.menu.repository.MenuRepository;
import com.cafehub.backend.domain.review.dto.ReviewDetail;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import com.cafehub.backend.domain.reviewLike.repository.ReviewLikeRepository;
import com.cafehub.backend.domain.reviewPhoto.dto.ReviewPhotoDetail;
import com.cafehub.backend.domain.reviewPhoto.repository.ReviewPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    // [FeedBack] DB, 네트워크 공부해보면 리팩토링 할 포인트가 보일거임, 특히 예외가 터질때? 성능이 좋아지지 않을까?
    // 스프링에 CS를 적용?

    // [Refactor Point] readOnly 옵션은 실제로 성능 비교를 해봐야 알 수 있음.
    // readOnly를 지원하지 않는 DB의 경우 쓸데 없이 네트워크만 한 번 더 타고 가서 나 읽기전용 모드요 라고 알려주는 역효과만 있음
    @Transactional(readOnly = true)
    public ResponseDTO<CafeListResponseDTO> getCafesByThemeAndSort(CafeListRequestDTO requestDTO) {

        return ResponseDTO.success(CafeListResponseDTO.from(cafeRepository.findCafesBySlice(requestDTO)));
    }

    /**
     *  [카페 정보 상세 보기 에서는 아래의 4가지 데이터 반환]
     *
     *  <모든 사용자에게 제공>
     *  1.카페의 기본 정보
     *  2.카페의 대표 메뉴들
     *  3.카페의 Top N개의 리뷰와 리뷰 사진들과 리뷰 작성자 프로필
     *
     *  <로그인 한 사용자의 경우>
     *  4. 해당 카페를 북마크 한 적이 있는지 여부 & 해당 카페의 Top N개의 리뷰에 대해서 좋아요를 누른적이 있는지 여부
     */
    @Transactional(readOnly = true)
    public ResponseDTO<CafeInfoResponseDTO> getCafeInfo(Long cafeId) {

        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(CafeNotFoundException::new);
        List<CafeInfoResponseDTO.BestMenuDetail> bestMenuList = menuRepository.findBestMenuList(cafeId);
        List<ReviewDetail> topNReview = reviewRepository.findTopNReviewsByCafeId(cafeId);
        updateReviewPhotoUrls(topNReview);

        Long loginMemberId = null;
        Boolean bookmarkChecked = false;

        if (jwtThreadLocalStorage.isLoginMember()) loginMemberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        // 로그인 한 회원의 경우 해당 카페를 북마크 한 적이 있는지 여부 & 해당 카페의 Top N개의 리뷰에 대해서 좋아요를 누른적이 있는지 여부 체크
        if(loginMemberId != null){
            bookmarkChecked = bookmarkRepository.existsByCafeIdAndMemberId(cafeId, loginMemberId);
            updateLikeStatusInReviews(topNReview, loginMemberId);
        }

        return ResponseDTO.success(
                CafeInfoResponseDTO.createFromCafeBookmarkMenuReview(
                        cafe,
                        bookmarkChecked,
                        bestMenuList,
                        topNReview
                )
        );

    }

    private void updateReviewPhotoUrls(List<ReviewDetail> topNReview){

        // Top N 리뷰의 ID 리스트 만들기
        List<Long> reviewIds = topNReview.stream()
                .map(ReviewDetail::getReviewId)
                .toList();

        // 한 번의 DB 커넥션으로 모든 사진 조회
        List<ReviewPhotoDetail> photoUrls = reviewPhotoRepository.findAllByReviewIdsIn(reviewIds);

        // 리뷰 ID에 따라 사진 URL을 매핑하는 맵 생성 후 세팅, 리뷰 Id : photoUrls 의 레퍼런스 구조
        Map<Long, List<String>> reviewPhotosMap = new HashMap<>();

        for (ReviewDetail rd : topNReview){
            reviewPhotosMap.put(rd.getReviewId(),new ArrayList<>());
        }

        for (ReviewPhotoDetail rp : photoUrls){
            reviewPhotosMap.get(rp.getReviewId()).add(rp.getPhotoUrl());
        }

        for (ReviewDetail x : topNReview) {
            x.setPhotoUrls(reviewPhotosMap.get(x.getReviewId()));
        }
    }

    private void updateLikeStatusInReviews(List<ReviewDetail> topNReview, Long loginMemberId) {

        // Top N 리뷰의 ID 리스트 만들기
        List<Long> reviewIds = topNReview.stream()
                .map(ReviewDetail::getReviewId)
                .toList();

        // 한 번의 DB 커넥션으로 사용자가 좋아요를 누른 리뷰 아이디 들을 검색
        List<Long> likeCheckedReviewIds =  reviewLikeRepository.findLikeCheckedReviewIds(loginMemberId, reviewIds);

        // 최대 N개의 리뷰들을 순회하게 되지만 N이 현재는 2이고 추후 아무리 높아져도 5 정도일 것으로 예상해서 그냥 전수 순회 로직을 짬
        for (ReviewDetail rd : topNReview){

            if(likeCheckedReviewIds.contains(rd.getReviewId())){
                rd.setLikeChecked(true);
            }
        }
    }
}