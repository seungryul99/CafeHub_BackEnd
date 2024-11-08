package com.cafehub.backend.domain.cafe.dto.response;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.entity.Theme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Setter
public class CafeListResponseDTO {

    private List<CafeDetails> cafeList;
    private Boolean isLast;
    private Integer currentPage;

    @Builder(access = AccessLevel.PRIVATE)
    private CafeListResponseDTO(List<CafeDetails> cafeList, Boolean isLast, Integer currentPage) {
        this.cafeList = cafeList;
        this.isLast = isLast;
        this.currentPage = currentPage;
    }

    public static CafeListResponseDTO from(Slice<CafeDetails> cafeDetails) {
        return CafeListResponseDTO.builder()
                .cafeList(cafeDetails.getContent())
                .isLast(cafeDetails.isLast())
                .currentPage(cafeDetails.getNumber())
                .build();
    }
}