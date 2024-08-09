package com.cafehub.backend.cafe.service;


import com.cafehub.backend.cafe.dto.CafeDetails;
import com.cafehub.backend.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.cafe.repository.CafeRepository;
import com.cafehub.backend.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CafeService {

    private final CafeRepository cafeRepository;


    @Transactional(readOnly = true)
    public ResponseDTO<CafeListResponseDTO> getCafesByThemeAndSort(CafeListRequestDTO requestDTO) {

        Slice<CafeDetails> cafeDetails =cafeRepository.findCafesBySlice(requestDTO);


        return ResponseDTO.success(
                new CafeListResponseDTO(
                        cafeDetails.getContent(),
                        cafeDetails.isLast(),
                        cafeDetails.getNumber()
                )
        );
    }
}
