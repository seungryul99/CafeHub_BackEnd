package com.cafehub.backend.domain.cafe.repository;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CafeRepositoryCustom {

    Slice<CafeDetails> findCafesBySlice (CafeListRequestDTO requestDTO);

    List<CafeDetails> findCafesByBookmarkList(List<Long> cafeIds);
}
