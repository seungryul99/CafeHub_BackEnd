package com.cafehub.backend.domain.bookmark.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkListResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import com.cafehub.backend.domain.bookmark.entity.Bookmark;
import com.cafehub.backend.domain.bookmark.exception.BookmarkDuplicateException;
import com.cafehub.backend.domain.bookmark.exception.BookmarkNotFoundException;
import com.cafehub.backend.domain.bookmark.repository.BookmarkRepository;
import com.cafehub.backend.domain.cafe.exception.CafeNotFoundException;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.member.login.exception.MemberNotFoundException;
import com.cafehub.backend.domain.member.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CafeRepository cafeRepository;
    private final LoginRepository loginRepository;
    private final JwtThreadLocalStorage jwtThreadLocalStorage;


    public ResponseDTO<BookmarkResponseDTO> bookmark(BookmarkRequestDTO requestDTO) {

        Long cafeId = requestDTO.getCafeId();
        Long memberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        if(bookmarkRepository.existsByCafeIdAndMemberId(cafeId, memberId)) throw new BookmarkDuplicateException();

        Bookmark bookmark = Bookmark.builder()
                .cafe(cafeRepository.findById(cafeId).orElseThrow(CafeNotFoundException::new))
                .member(loginRepository.findById(memberId).orElseThrow(MemberNotFoundException::new))
                .build();

        bookmarkRepository.save(bookmark);

        return ResponseDTO.success(new BookmarkResponseDTO(cafeId));
    }


    public ResponseDTO<BookmarkResponseDTO> deleteBookmark(BookmarkRequestDTO requestDTO) {

        Long cafeId = requestDTO.getCafeId();
        Long memberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        if(!cafeRepository.existsById(cafeId)) throw new CafeNotFoundException();
        if(!bookmarkRepository.existsByCafeIdAndMemberId(cafeId, memberId)) throw new BookmarkNotFoundException();

        bookmarkRepository.deleteByCafeIdAndMemberId(cafeId, memberId);

        return ResponseDTO.success(new BookmarkResponseDTO(cafeId));
    }



    public ResponseDTO<BookmarkListResponseDTO> getBookmarkList() {

        Long memberId = jwtThreadLocalStorage.getMemberIdFromJwt();

        // 가져와야 할 모든 정보는 Cafe 테이블에 있음
        // 북마크 테이블에서 사용자의 memberId로 전부 조회후 카페 Id 추출
        List<Bookmark> bookmarkList = bookmarkRepository.findAllByMemberId(memberId);


        // Bookmark 에서 CafeId를 추출, but 여기서 계속 지연로딩으로 인해서 DB Connection 발생, Refactor 포인트
        // 추후 성능 테스트를 위해서 리팩토링 하지 않고 남겨둠. 추후 fetch join 방식으로 변경 예정
        List<Long> cafeIds = bookmarkList.stream()
                .map(bookmark -> bookmark.getCafe().getId())
                .toList();


        // 해당 memberId를 통해서 In으로 한번에 카페에서 조회
        return ResponseDTO.success(BookmarkListResponseDTO.builder()
                .cafeList(cafeRepository.findCafesByBookmarkList(cafeIds))
                .build());
    }
}