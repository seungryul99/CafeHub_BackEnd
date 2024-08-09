package com.cafehub.backend.cafe.dto.response;


import com.cafehub.backend.cafe.dto.CafeDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CafeListResponseDTO {

    private List<CafeDetails> cafeList;

    private Boolean isLast;

    private Integer currentPage;
}
