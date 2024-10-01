package com.cafehub.backend.domain.cafe.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CafeListRequestDTO {

    private String theme;

    private String sortType;

    private Integer currentPage;
}
