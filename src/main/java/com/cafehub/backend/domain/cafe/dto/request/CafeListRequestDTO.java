package com.cafehub.backend.domain.cafe.dto.request;

import lombok.*;

@Getter
@Setter
public class CafeListRequestDTO {

    private String theme;
    private String sortType;
    private Integer currentPage;

    @Builder(access = AccessLevel.PRIVATE)
    private CafeListRequestDTO(String theme, String sortType, Integer currentPage) {
        this.theme = theme;
        this.sortType = sortType;
        this.currentPage = currentPage;
    }

    public static CafeListRequestDTO themeSortPageOf (String theme, String sortType, Integer currentPage){
        return CafeListRequestDTO.builder()
                .theme(theme)
                .sortType(sortType)
                .currentPage(currentPage)
                .build();
    }
}
