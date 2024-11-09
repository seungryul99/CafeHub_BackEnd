package com.cafehub.backend.domain.menu.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.menu.dto.response.MenuListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Validated
@Tag(name = "2. [메뉴]", description = "Menu API")
public interface MenuControllerAPI {

    @Operation(
            summary = "카페 메뉴 전체조회",
            description = "사용자가 선택한 카페의 모든 메뉴를 조회합니다. 페이징 없음.",
            parameters = {
                    @Parameter(name = "cafeId", description = "1 이상의 정수", example = "1")
            })
    @GetMapping("/cafe/{cafeId}/menu")
    ResponseEntity<ResponseDTO<MenuListResponse>> getAllMenuOfCafe(@Positive(message = "전체메뉴를 조회하려는 카페의 ID는 1이상의 정수여야 합니다")Long cafeId);
}
