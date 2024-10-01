package com.cafehub.backend.domain.cafe.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CafeService {

    private final CafeRepository cafeRepository;


    // [Refactor Point] readOnly 옵션은 실제로 성능 비교를 해봐야 알 수 있음.
    // readOnly를 지원하지 않는 DB의 경우 쓸데 없이 네트워크만 한 번 더 타고 가서 나 읽기전용 모드요 라고 알려주는 역효과만 있음
    @Transactional(readOnly = true)
    public ResponseDTO<CafeListResponseDTO> getCafesByThemeAndSort(CafeListRequestDTO requestDTO) {

        Slice<CafeListResponseDTO.CafeDetails> cafeDetails =cafeRepository.findCafesBySlice(requestDTO);


        return ResponseDTO.success(CafeListResponseDTO.builder()
                .cafeList(cafeDetails.getContent())
                .isLast(cafeDetails.isLast())
                .currentPage(cafeDetails.getNumber())
                .build());
    }
}