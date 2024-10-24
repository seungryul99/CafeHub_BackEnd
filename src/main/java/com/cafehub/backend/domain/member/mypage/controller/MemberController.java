package com.cafehub.backend.domain.member.mypage.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.request.MemberNicknameUpdateRequestDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageUpdateResponseDTO;
import com.cafehub.backend.domain.member.mypage.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/mypage")
public class MemberController {

    private final MemberService memberService;


    @GetMapping
    public ResponseEntity<ResponseDTO<MyPageResponseDTO>> getMyPage(){

        return ResponseEntity.ok(memberService.getMyPage());
    }


    @PostMapping("/nickname")
    public ResponseEntity<ResponseDTO<MyPageUpdateResponseDTO>> updateNickname(@RequestBody MemberNicknameUpdateRequestDTO requestDTO){

        return ResponseEntity.ok(memberService.updateNickname(requestDTO));
    }

    @PostMapping(value = "/profileImg",  consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<MyPageUpdateResponseDTO>> updateProfileImage(@RequestParam MultipartFile profileImg) {
        log.info("사진 이미지 전달 정상 도착");
        log.info("전송된 파일 이름 : " + profileImg.getOriginalFilename());
        log.info("사진 용량 : " + profileImg.getSize());

        return ResponseEntity.ok(memberService.updateProfileImg(profileImg));
    }

}

