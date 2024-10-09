package com.cafehub.backend.domain.cafe.dto.response;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.entity.Theme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CafeListResponseDTO {

    private List<CafeDetails> cafeList;

    private Boolean isLast;

    private Integer currentPage;
}
