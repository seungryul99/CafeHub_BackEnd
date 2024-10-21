package com.cafehub.backend.domain.reviewPhoto.repository;

import com.cafehub.backend.domain.reviewPhoto.dto.ReviewPhotoDetail;
import com.cafehub.backend.domain.reviewPhoto.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

    @Query("SELECT new com.cafehub.backend.domain.reviewPhoto.dto.ReviewPhotoDetail(rp.review.id, rp.reviewPhoto.url) " +
            "FROM ReviewPhoto rp WHERE rp.review.id IN :reviewIds")
    List<ReviewPhotoDetail> findAllByReviewIdsIn(@Param("reviewIds") List<Long> reviewIds);
}
