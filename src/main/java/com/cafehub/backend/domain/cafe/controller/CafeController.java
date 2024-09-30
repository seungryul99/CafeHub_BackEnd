package com.cafehub.backend.domain.cafe.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.domain.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeController {

    private final CafeService cafeService;


    @GetMapping("/cafeList/{theme}/{sortedByType}/{currentPage}")
    public ResponseEntity<ResponseDTO<CafeListResponseDTO>> getCafeList(@PathVariable("theme") String theme,
                                                                        @PathVariable("sortedByType") String sortType,
                                                                        @PathVariable("currentPage") int currentPage){


        // [Refactor Point] 파라미터 취합 -> DTO로 생성, 이 로직을 컨트롤러가 아닌 다른 곳에서 가져갈 수 있지 않을까?
        CafeListRequestDTO requestDTO = CafeListRequestDTO.builder()
                .theme(theme)
                .sortType(sortType)
                .currentPage(currentPage)
                .build();


        return ResponseEntity.status(HttpStatus.OK)
                .body(cafeService.getCafesByThemeAndSort(requestDTO));
    }

}
