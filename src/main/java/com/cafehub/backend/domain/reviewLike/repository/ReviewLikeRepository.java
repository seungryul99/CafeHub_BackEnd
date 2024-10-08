package com.cafehub.backend.domain.reviewLike.repository;

import com.cafehub.backend.domain.reviewLike.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    List<ReviewLike> findByMemberIdAndReviewIdIn(Long id, List<Long> reviewIds);
}
