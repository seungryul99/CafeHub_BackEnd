package com.cafehub.backend.domain.cafe.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import com.cafehub.backend.domain.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeController implements CafeControllerDocs {

    private final CafeService cafeService;



    @GetMapping("/cafeList/{theme}/{sortedType}/{currentPage}")
    public ResponseEntity<ResponseDTO<CafeListResponseDTO>> getCafeList(@PathVariable("theme") String theme,
                                                                        @PathVariable("sortedType") String sortType,
                                                                        @PathVariable("currentPage") int currentPage){


        // [Refactor Point] 파라미터 취합 -> DTO로 생성, 이 로직을 컨트롤러가 아닌 다른 곳에서 가져갈 수 있지 않을까?
        CafeListRequestDTO requestDTO = CafeListRequestDTO.builder()
                .theme(theme)
                .sortType(sortType)
                .currentPage(currentPage)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(cafeService.getCafesByThemeAndSort(requestDTO));
    }




    @GetMapping("/optional-auth/cafe/{cafeId}")
    public ResponseEntity<ResponseDTO<CafeInfoResponseDTO>> getCafeInfo(@PathVariable("cafeId") Long cafeId){

        return ResponseEntity.status(HttpStatus.OK).body(cafeService.getCafeInfo(cafeId));
    }
}
