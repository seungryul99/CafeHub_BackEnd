package com.cafehub.backend.domain.member.mypage.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageResponseDTO;
import com.cafehub.backend.domain.member.mypage.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/mypage")
    public ResponseEntity<ResponseDTO<MyPageResponseDTO>> getMyPage(){

        return ResponseEntity.ok(memberService.getMyPage());
    }

}

