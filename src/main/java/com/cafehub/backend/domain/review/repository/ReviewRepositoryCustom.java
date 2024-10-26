package com.cafehub.backend.domain.review.repository;

import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.review.dto.ReviewDetail;
import com.cafehub.backend.domain.review.dto.request.AllReviewGetRequestDTO;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface ReviewRepositoryCustom {

    List<ReviewDetail> findTopNReviewsByCafeId(Long cafeId, int topNReviewSize);

    Slice<ReviewDetail> findReviewsBySlice(AllReviewGetRequestDTO requestDTO);
}
