package com.cafehub.backend.domain.cafe.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.bookmark.repository.BookmarkRepository;
import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.menu.repository.MenuRepository;
import com.cafehub.backend.domain.review.dto.ReviewDetail;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import com.cafehub.backend.domain.reviewLike.repository.ReviewLikeRepository;
import com.cafehub.backend.domain.reviewPhoto.dto.ReviewPhotoDetail;
import com.cafehub.backend.domain.reviewPhoto.repository.ReviewPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cafehub.backend.common.constants.CafeHubConstants.TOP_N_REVIEW_SIZE;

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
     *  3.카페의 Top N개의 리뷰와 리뷰 사진들
     *
     *  <로그인 한 사용자의 경우>
     *  4. 해당 카페를 북마크 한 적이 있는지 여부 & 해당 카페의 Top N개의 리뷰에 대해서 좋아요를 누른적이 있는지 여부
     *
     *  <변경사항>
     *  + 리뷰 작성자 프로필 추가
     */
    @Transactional(readOnly = true)
    public ResponseDTO<CafeInfoResponseDTO> getCafeInfo(Long cafeId) {


        // 1.카페의 기본 정보
        // Optional 처리는 나중에, 파라미터로 부터 받은 카페 ID를 통해서 DB에서 해당 카페의 기본 정보를 가져옴
        Cafe cafe = cafeRepository.findById(cafeId).get();

        // 2.카페의 대표 메뉴들
        // 해당 카페의 대표 메뉴 리스트를 가져옴, 바로 DTO 레벨로 조회
        List<CafeInfoResponseDTO.BestMenuDetail> bestMenuList = menuRepository.findBestMenuList(cafeId);

        // 3.카페의 Top N개의 리뷰와 리뷰 사진들
        // 좋아요를 기준으로 TOP N 개의 리뷰를 가져옴, 단 이때 사용자가 각 리뷰에 대해 좋아요를 눌렀는지 체크여부와 사진들은 안가져옴
        List<ReviewDetail> topNReview = reviewRepository.findTopNReviewsByCafeId(cafeId, TOP_N_REVIEW_SIZE);
        updateReviewPhotoUrls(topNReview);


        // ThreadLocal에서 현재 회원이 로그인 했는지 확인 후 정보를 가지고 옴.
        Long loginMemberId = null;
        Boolean bookmarkChecked = false;

        if (jwtThreadLocalStorage.isLoginMember()) loginMemberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        // 4.로그인 한 회원의 경우 해당 카페를 북마크 한 적이 있는지 여부 & 해당 카페의 Top N개의 리뷰에 대해서 좋아요를 누른적이 있는지 여뷰
        if(loginMemberId != null){
            bookmarkChecked = bookmarkRepository.existsByCafeIdAndMemberId(cafeId, loginMemberId);
            updateLikeStatusInReviews(topNReview, loginMemberId);
        }

        return ResponseDTO.success(CafeInfoResponseDTO.of(cafe, bookmarkChecked, bestMenuList, topNReview));
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