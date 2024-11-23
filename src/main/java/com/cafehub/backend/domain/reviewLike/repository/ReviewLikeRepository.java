package com.cafehub.backend.domain.reviewLike.repository;

import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.reviewLike.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    void deleteAllByReviewId(Long reviewId);

    @Query("SELECT rl.review.id FROM ReviewLike rl WHERE rl.member.id = :memberId AND rl.review.id IN :reviewIds")
    List<Long> findLikeCheckedReviewIds(@Param("memberId") Long memberId, @Param("reviewIds") List<Long> reviewIds);
    void deleteByReviewIdAndMemberId(Long reviewId, Long memberIdFromJwt);

    boolean existsByReviewIdAndMemberId(Long review_id, Long member_id);
}
