package com.cafehub.backend.domain.cafe.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Cafe", description = "Cafe 관련 API 모음")
public interface CafeControllerSpec {



    @Operation(
            summary = "카페리스트 조회",
            description = "사용자가 선택한 테마와 정렬 타입을 기반으로 카페 리스트를 조회합니다. 페이지네이션을 위해 현재 페이지를 지정할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 카페 리스트를 조회한 경우"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인해 조회 실패")
            },
            parameters = {
                    @Parameter(name = "theme", description = "Date | Meet | Dessert | Study | All", example = "Date"),
                    @Parameter(name = "sortedByType", description = "name | name_d | reviewNum | reviewNum_a | rating | rating_a", example = "name"),
                    @Parameter(name = "currentPage", description = "0 이상의 정수", example = "1")
            })

    @GetMapping("/cafeList/{theme}/{sortedByType}/{currentPage}")
    ResponseEntity<ResponseDTO<CafeListResponseDTO>> getCafeList(@PathVariable("theme") String theme,
                                                                 @PathVariable("sortedByType") String sortType,
                                                                 @PathVariable("currentPage") int currentPage);


}
