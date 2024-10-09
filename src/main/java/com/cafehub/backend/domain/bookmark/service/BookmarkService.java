package com.cafehub.backend.domain.bookmark.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.request.BookmarkRequestDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkListResponseDTO;
import com.cafehub.backend.domain.bookmark.dto.response.BookmarkResponseDTO;
import com.cafehub.backend.domain.bookmark.entity.Bookmark;
import com.cafehub.backend.domain.bookmark.repository.BookmarkRepository;
import com.cafehub.backend.domain.cafe.repository.CafeRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.jwt.JwtPayloadReader;
import com.cafehub.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Long memberId = getMemberIdFromJwt(requestDTO.getJwtAccessToken());


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
        Long memberId = getMemberIdFromJwt(requestDTO.getJwtAccessToken());


        bookmarkRepository.deleteByCafeIdAndMemberId(cafeId, memberId);

        return ResponseDTO.success(BookmarkResponseDTO.builder()
                .cafeId(cafeId)
                .build());
    }



    @Transactional(readOnly = true)
    public ResponseDTO<BookmarkListResponseDTO> getBookmarkList(String jwtAccessToken) {

        Long memberId = getMemberIdFromJwt(jwtAccessToken);

        
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


    private Long getMemberIdFromJwt(String jwtAccessToken){
        return jwtPayloadReader.getMemberId(jwtAccessToken);
    }
}
