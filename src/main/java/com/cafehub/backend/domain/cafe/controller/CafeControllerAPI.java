package com.cafehub.backend.domain.cafe.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.CafeListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Validated
@Tag(name = "1. [카페]", description = "Cafe API")
public interface CafeControllerAPI {

    @Operation(
            summary = "카페 리스트 조회",
            description = "사용자가 선택  한 테마와 정렬 기준으로 카페 리스트를 조회합니다. 1페이지에 10개의 카페를 받을 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 카페 리스트를 조회한 경우"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인해 조회 실패")
            },
            parameters = {
                    @Parameter(name = "theme", description = "Date | Meet | Dessert | Study | All", example = "Date"),
                    @Parameter(name = "sortedType", description = "name | name_d | reviewNum | reviewNum_a | rating | rating_a", example = "name"),
                    @Parameter(name = "currentPage", description = "0 이상의 정수", example = "0")
            })
    @GetMapping("/cafeList/{theme}/{sortedType}/{currentPage}")
    ResponseEntity<ResponseDTO<CafeListResponseDTO>> getCafeList(@Pattern(regexp = "^(Date|Meet|Dessert|Study|All)$", message = "카페리스트 검색 조건중 테마가 잘못 입력 되었습니다") String theme,
                                                                 @Pattern(regexp = "^(name_asc|name_desc|reviewNum_asc|reviewNum_desc|rating_asc|rating_desc)$", message = "카페리스트 검색 조건중 정렬기준이 잘못 입력 되었습니다") String sortType,
                                                                 @PositiveOrZero(message = "카페리스트 검색 조건중 현재 페이지는 0 이상의 정수만 입력해야 합니다") int currentPage);


    @Operation(
            summary = "카페 상세정보 조회",
            description = "사용자가 선택한 카페의 id를 기반으로 해당 카페의 정보들을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 카페를 조회한 경우"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인해 조회 실패")
            },
            parameters = {
                    @Parameter(name = "cafeId", description = "1 이상의 정수", example = "1")
            })
    ResponseEntity<ResponseDTO<CafeInfoResponseDTO>> getCafeInfo(@PathVariable("cafeId") Long cafeId);
}