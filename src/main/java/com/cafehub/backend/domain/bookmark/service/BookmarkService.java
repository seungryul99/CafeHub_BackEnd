package com.cafehub.backend.domain.bookmark.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import com.cafehub.backend.domain.bookmark.entity.Bookmark;
import com.cafehub.backend.domain.bookmark.repository.BookmarkRepository;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final CafeRepository cafeRepository;

    private final MemberRepository memberRepository;

    private final JwtPayloadReader jwtPayloadReader;


    
    // 예외 처리 나중에
    public ResponseDTO<BookmarkResponseDTO> bookmark(BookmarkRequestDTO requestDTO) {

        Long cafeId = requestDTO.getCafeId();
        Long memberId = jwtPayloadReader.getMemberId(requestDTO.getJwtAccessToken());


        Bookmark bookmark = Bookmark.builder()
                .cafe(cafeRepository.findById(cafeId).get())
                .member(memberRepository.findById(memberId).get())
                .build();

        bookmarkRepository.save(bookmark);


        return ResponseDTO.success(BookmarkResponseDTO.builder()
                .cafeId(cafeId)
                .build());
    }



    
    
    // 예외 처리 나중에
    public ResponseDTO<BookmarkResponseDTO> deleteBookmark(BookmarkRequestDTO requestDTO) {

        Long cafeId = requestDTO.getCafeId();
        Long memberId = jwtPayloadReader.getMemberId(requestDTO.getJwtAccessToken());

        bookmarkRepository.deleteByCafeIdAndMemberId(cafeId, memberId);

        return ResponseDTO.success(BookmarkResponseDTO.builder()
                .cafeId(cafeId)
                .build());
    }
}
