package com.cafehub.backend.domain.bookmark.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface BookmarkControllerDocs {


    @Operation(
            summary = "카페 북마크하기 / 북마크 취소하기",
            description = "사용자가 마음에 드는 카페를 북마크하기 or 이미 북마크된 카페의 북마크 취소하기",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            },
            parameters = {
                    // 스와거는 나중에
            })
    @PostMapping("/bookmark")
    ResponseEntity<ResponseDTO<BookmarkResponseDTO>> bookmarkManage(@RequestBody BookmarkRequestDTO requestDTO);
}
