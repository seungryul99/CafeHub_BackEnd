package com.cafehub.backend.domain.member.mypage.service;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageResponseDTO;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private final MemberRepository memberRepository;


    public ResponseDTO<MyPageResponseDTO> getMyPage() {

        Member member = memberRepository.findById(jwtThreadLocalStorage.getMemberIdFromJwt()).get();

        MyPageResponseDTO res = MyPageResponseDTO.builder().
                memberImgUrl(member.getProfileImg().getUrl()).
                nickname(member.getNickname()).
                email(member.getEmail()).
                build();

        return ResponseDTO.success(res);
    }
}
