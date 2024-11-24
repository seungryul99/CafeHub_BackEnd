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
@Tag(name = "2. [카페]", description = "Cafe API")
public interface CafeControllerAPI {@Operation(
        summary = "카페 리스트 조회",
        description = "사용자가 선택  한 테마와 정렬 기준으로 카페 리스트를 조회합니다. 1페이지에 10개의 카페를 받을 수 있습니다.",
        parameters = {
                @Parameter(name = "theme", description = "Date | Meet | Dessert | Study | All", example = "Date"),
                @Parameter(name = "sortedType", description = "name_asc | name_desc | reviewNum_desc | reviewNum_asc | rating_desc | rating_asc", example = "name_asc"),
                @Parameter(name = "currentPage", description = "0 이상의 정수", example = "0")
        })

    // [FeedBack] 빈 밸리데이션 어노테이션으로 빼는게 맞다.

    @GetMapping("/cafeList/{theme}/{sortedType}/{currentPage}")
    ResponseEntity<ResponseDTO<CafeListResponseDTO>> getCafeList(@Pattern(regexp = "^(Date|Meet|Dessert|Study|All)$", message = "카페리스트 검색 조건중 테마가 잘못 입력 되었습니다") String theme,
                                                                 @Pattern(regexp = "^(name_asc|name_desc|reviewNum_asc|reviewNum_desc|rating_asc|rating_desc)$", message = "카페리스트 검색 조건중 정렬기준이 잘못 입력 되었습니다") String sortType,
                                                                 @PositiveOrZero(message = "카페리스트 검색 조건중 현재 페이지는 0 이상의 정수만 입력해야 합니다") int currentPage);



    @Operation(
            summary = "카페 상세정보 조회",
            description = "사용자가 선택한 카페의 Id에 해당하는 카페의 정보들을 조회합니다.",
            parameters = {
                    @Parameter(name = "cafeId", description = "1 이상의 정수", example = "1")
            })
    @GetMapping("/optional-auth/cafe/{cafeId}")
    ResponseEntity<ResponseDTO<CafeInfoResponseDTO>> getCafeInfo(@Positive(message = "조회 하려는 카페의 ID는 1이상의 정수여야 합니다") Long cafeId);

}
