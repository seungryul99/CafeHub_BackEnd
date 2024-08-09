package com.cafehub.backend.cafe.controller;

import com.cafehub.backend.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.cafe.service.CafeService;
import com.cafehub.backend.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
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

        CafeListRequestDTO requestDTO = new CafeListRequestDTO(theme,sortType,currentPage);

        return ResponseEntity.ok(cafeService.getCafesByThemeAndSort(requestDTO));
    }



}
