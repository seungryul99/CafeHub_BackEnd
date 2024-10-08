package com.cafehub.backend.domain.review.repository;

import com.cafehub.backend.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    // cafe 에서 필요함
    @Query("SELECT DISTINCT r FROM Review r JOIN FETCH r.member LEFT JOIN FETCH r.reviewPhotos WHERE r.cafe.id = :cafeId")
    List<Review> findAllByCafeIdWithMemberAndPhotos(@Param("cafeId") Long cafeId, Pageable pageable);
}
