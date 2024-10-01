package com.cafehub.backend.domain.cafe.repository;

import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import org.springframework.data.domain.Slice;

public interface CafeRepositoryCustom {

    Slice<CafeListResponseDTO.CafeDetails> findCafesBySlice (CafeListRequestDTO requestDTO);
}
