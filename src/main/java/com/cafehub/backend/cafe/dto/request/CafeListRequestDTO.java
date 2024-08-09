package com.cafehub.backend.cafe.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CafeListRequestDTO {

    private String theme;

    private String sortType;

    private Integer currentPage;
}
