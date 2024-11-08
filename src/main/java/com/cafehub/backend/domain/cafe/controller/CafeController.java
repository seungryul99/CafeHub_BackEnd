package com.cafehub.backend.domain.cafe.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.domain.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeController implements CafeControllerAPI {

    private final CafeService cafeService;


    @GetMapping("/cafeList/{theme}/{sortedType}/{currentPage}")
    public ResponseEntity<ResponseDTO<CafeListResponseDTO>> getCafeList(@PathVariable("theme") String theme,
                                                                        @PathVariable("sortedType") String sortType,
                                                                        @PathVariable("currentPage") int currentPage){

        CafeListRequestDTO requestDTO = CafeListRequestDTO.themeSortPageOf(theme,sortType,currentPage);

        return ResponseEntity.status(HttpStatus.OK).body(cafeService.getCafesByThemeAndSort(requestDTO));
    }

    @GetMapping("/optional-auth/cafe/{cafeId}")
    public ResponseEntity<ResponseDTO<CafeInfoResponseDTO>> getCafeInfo(@PathVariable("cafeId") Long cafeId){

        return ResponseEntity.status(HttpStatus.OK).body(cafeService.getCafeInfo(cafeId));
    }
}
