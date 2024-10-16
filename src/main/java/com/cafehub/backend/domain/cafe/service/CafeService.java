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
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import com.cafehub.backend.domain.menu.repository.MenuRepository;
import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import com.cafehub.backend.domain.reviewLike.entity.ReviewLike;
import com.cafehub.backend.domain.reviewLike.repository.ReviewLikeRepository;
import com.cafehub.backend.domain.reviewPhotos.dto.PhotoUrlResponseDTO;
import com.cafehub.backend.domain.reviewPhotos.entity.ReviewPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CafeService {

    private final CafeRepository cafeRepository;

    private final MenuRepository menuRepository;

    private final ReviewRepository reviewRepository;

    private final BookmarkRepository bookmarkRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final MemberRepository memberRepository;

    private final JwtThreadLocalStorage jwtThreadLocalStorage;


    private static final int TOP_REVIEW_SIZE = 2;


    // [Refactor Point] readOnly 옵션은 실제로 성능 비교를 해봐야 알 수 있음.
    // readOnly를 지원하지 않는 DB의 경우 쓸데 없이 네트워크만 한 번 더 타고 가서 나 읽기전용 모드요 라고 알려주는 역효과만 있음
    @Transactional(readOnly = true)
    public ResponseDTO<CafeListResponseDTO> getCafesByThemeAndSort(CafeListRequestDTO requestDTO) {

        Slice<CafeDetails> cafeDetails =cafeRepository.findCafesBySlice(requestDTO);

        return ResponseDTO.success(CafeListResponseDTO.builder()
                .cafeList(cafeDetails.getContent())
                .isLast(cafeDetails.isLast())
                .currentPage(cafeDetails.getNumber())
                .build());
    }


    @Transactional(readOnly = true)
    public ResponseDTO<CafeInfoResponseDTO> getCafeInfo(Long cafeId) {

        // Optional 처리는 나중에
        Cafe cafe = cafeRepository.findById(cafeId).get();

        // 유효하지 않은 JWT에 따른 예외 처리는 컨트롤러단에서 나중에, 일단 유효한 JWT만 들어온다고 가정

        Long loginMemberId = null;
        Member loginMember = null;

        if(jwtThreadLocalStorage.isLoginMember()){
            loginMemberId = jwtThreadLocalStorage.getMemberIdFromJwt();
            log.info("추출한 MemberId : " + loginMemberId);

            loginMember = memberRepository.findById(loginMemberId).get();
        }


        Boolean bookmarkChecked = isBookmarkChecked(cafe.getId(),loginMember);

        List<CafeInfoResponseDTO.BestMenuDetail> bestMenuList = getBestMenuList(cafe.getId());

        List<Review> topNReview = getTopNReviews(cafe.getId());

        Set<Long> likedReviewIds = getLikedReviewIds(topNReview, loginMember);

        List<CafeInfoResponseDTO.BestReviewDetail> bestReviewList = getBestReviewList(topNReview, likedReviewIds);


        return ResponseDTO.success(CafeInfoResponseDTO.builder()
                .cafeId(cafe.getId())
                .cafePhotoUrl(cafe.getCafeImg().getUrl())
                .cafeName(cafe.getName())
                .cafeTheme(cafe.getTheme())
                .cafeReviewCnt(cafe.getReviewCnt())
                .cafeOperationHour(cafe.getOperationHours())
                .cafeAddress(cafe.getAddress())
                .cafePhone(cafe.getPhone())
                .cafeRating(cafe.getRating())
                .bookmarkChecked(bookmarkChecked)
                .bestMenuList(bestMenuList)
                .bestReviewList(bestReviewList)
                .build());
    }

    private Boolean isBookmarkChecked(Long cafeId, Member loginMember) {
        if (loginMember != null) {
            return bookmarkRepository.existsByCafeIdAndMemberId(cafeId, loginMember.getId());
        }
        return false;
    }

    private List<CafeInfoResponseDTO.BestMenuDetail> getBestMenuList(Long cafeId) {
        return menuRepository.getBestMenuList(cafeId);
    }

    
    // 지금은 2개의 대표 리뷰를 보여주지만 추후 N개로 확장 될 수 있음
    private List<Review> getTopNReviews(Long cafeId) {
        return reviewRepository.findAllByCafeIdWithMemberAndPhotos(cafeId,
                PageRequest.of(0, TOP_REVIEW_SIZE, Sort.by(Sort.Direction.DESC, "likeCnt")));
    }


    private Set<Long> getLikedReviewIds(List<Review> topNReview, Member loginMember) {
        if (loginMember != null) {
            List<Long> reviewIds = topNReview.stream()
                    .map(Review::getId)
                    .toList();

            List<ReviewLike> likeReviews = reviewLikeRepository.findByMemberIdAndReviewIdIn(loginMember.getId(), reviewIds);
            return likeReviews.stream()
                    .map(likeReview -> likeReview.getReview().getId())
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }


    private List<CafeInfoResponseDTO.BestReviewDetail> getBestReviewList(List<Review> topNReview, Set<Long> likedReviewIds) {

        return topNReview.stream()
                .map(review -> {
                    List<ReviewPhoto> photos = review.getReviewPhotos();
                    List<PhotoUrlResponseDTO> photoUrlResponseList = photos.stream()
                            .map(photo -> new PhotoUrlResponseDTO(photo.getReviewPhoto().getUrl()))
                            .toList();

                    return CafeInfoResponseDTO.BestReviewDetail.builder()
                            .reviewId(review.getId())
                            .author(review.getMember().getNickname())
                            .reviewRating(review.getRating())
                            .reviewContent(review.getContent())
                            .reviewCreateAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .likeCnt(review.getLikeCnt())
                            .commentCnt(review.getCommentCnt())
                            .likeChecked(likedReviewIds.contains(review.getId()))
                            .photoUrls(photoUrlResponseList)
                            .build();
                })
                .toList();
    }
}