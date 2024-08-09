package com.cafehub.backend.cafe.repository;

import com.cafehub.backend.cafe.dto.CafeDetails;
import com.cafehub.backend.cafe.dto.request.CafeListRequestDTO;
import org.springframework.data.domain.Slice;

public interface CafeRepositoryCustom {

    Slice<CafeDetails> findCafesBySlice (CafeListRequestDTO requestDTO);
}
