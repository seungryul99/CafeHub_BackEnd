package com.cafehub.backend.domain.review.repository;

import com.cafehub.backend.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {


    List<Review> findAllByMemberId(Long memberId);
}