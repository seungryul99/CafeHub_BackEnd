package com.cafehub.backend.domain.bookmark.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkListResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import com.cafehub.backend.domain.bookmark.service.BookmarkService;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class BookmarkController implements BookmarkControllerDocs{

    private final BookmarkService bookmarkService;


    @PostMapping("/bookmark")
    public ResponseEntity<ResponseDTO<BookmarkResponseDTO>> bookmarkManage(@RequestBody BookmarkRequestDTO requestDTO){

        log.info("북마크 하기 요청 발생");
        log.info("북마크 체크 여부 : " + requestDTO.getBookmarkChecked());


        if (requestDTO.getBookmarkChecked()) return ResponseEntity.ok(bookmarkService.bookmark(requestDTO));
        else return ResponseEntity.ok(bookmarkService.deleteBookmark(requestDTO));
    }



    @GetMapping("/bookmarks")
    public ResponseEntity<ResponseDTO<BookmarkListResponseDTO>> getBookmarkList(){

        log.info("사용자의 북마크 불러오기 요청 발생");

        return ResponseEntity.ok(bookmarkService.getBookmarkList());
    }



}