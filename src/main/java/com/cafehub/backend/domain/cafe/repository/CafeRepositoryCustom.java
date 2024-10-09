package com.cafehub.backend.domain.cafe.repository;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CafeRepositoryCustom {

    Slice<CafeDetails> findCafesBySlice (CafeListRequestDTO requestDTO);

}
