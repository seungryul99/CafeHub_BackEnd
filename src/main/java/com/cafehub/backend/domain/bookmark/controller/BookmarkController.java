package com.cafehub.backend.domain.bookmark.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import com.cafehub.backend.domain.bookmark.service.BookmarkService;
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
    public ResponseEntity<ResponseDTO<BookmarkResponseDTO>> bookmarkManage(@RequestBody BookmarkRequestDTO requestDTO,
                                                                           @RequestHeader(value = "Authorization", required = false) String jwtAccessToken){


        log.info("북마크 하기 요청 발생");
        log.info("북마크 체크 여부 : " + requestDTO.getBookmarkChecked());

        requestDTO.setJwtAccessToken(jwtAccessToken);

        if (requestDTO.getBookmarkChecked()) return ResponseEntity.ok(bookmarkService.bookmark(requestDTO));
        else return ResponseEntity.ok(bookmarkService.deleteBookmark(requestDTO));
    }



}