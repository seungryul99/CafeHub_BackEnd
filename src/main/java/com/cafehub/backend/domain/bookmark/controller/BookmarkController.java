package com.cafehub.backend.domain.bookmark.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkListResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import com.cafehub.backend.domain.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class BookmarkController{

    private final BookmarkService bookmarkService;


    // @RequestBody에서 Long cafeId 에 1.2 같은 소수를 입력해도 알아서 정수부만 떼서 변환시켜 문제가 발생함.
    // ex) cafeId : 1.2 는 1로 변환됨
    // ex) cafeId : "1.2"는 변환되지 못하고 에러가 발생함
    @PostMapping("/bookmark")
    public ResponseEntity<ResponseDTO<BookmarkResponseDTO>> bookmarkManage(@Valid @RequestBody BookmarkRequestDTO requestDTO){

        if (requestDTO.getBookmarkChecked()) return ResponseEntity.ok(bookmarkService.bookmark(requestDTO));
        else return ResponseEntity.ok(bookmarkService.deleteBookmark(requestDTO));
    }


    @GetMapping("/bookmarks")
    public ResponseEntity<ResponseDTO<BookmarkListResponseDTO>> getBookmarkList(){

        return ResponseEntity.ok(bookmarkService.getBookmarkList());
    }

}