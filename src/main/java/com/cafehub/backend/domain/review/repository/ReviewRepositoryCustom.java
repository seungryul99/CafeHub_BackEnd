package com.cafehub.backend.domain.review.repository;

import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;

import java.util.List;
import java.util.Map;

public interface ReviewRepositoryCustom {

    List<CafeInfoResponseDTO.BestReviewDetail> findTopNReviewsByCafeId(Long cafeId, int topNReviewSize);
}
