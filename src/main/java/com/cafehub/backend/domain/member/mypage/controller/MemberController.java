package com.cafehub.backend.domain.member.mypage.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.request.MemberNicknameUpdateRequestDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageResponseDTO;
import com.cafehub.backend.domain.member.mypage.dto.response.MyPageUpdateResponseDTO;
import com.cafehub.backend.domain.member.mypage.exception.InvalidMemberProfileImgExtensionException;
import com.cafehub.backend.domain.member.mypage.exception.TooLongMemberProfileImgNameException;
import com.cafehub.backend.domain.member.mypage.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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
    public ResponseEntity<ResponseDTO<MyPageUpdateResponseDTO>> updateNickname(@Valid @RequestBody MemberNicknameUpdateRequestDTO requestDTO){

        return ResponseEntity.ok(memberService.updateNickname(requestDTO));
    }

    @PostMapping(value = "/profileImg",  consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<MyPageUpdateResponseDTO>> updateProfileImage(@RequestParam MultipartFile profileImg) {


        /**
         *  확장자 검증은 추후 커스텀 검증 어노테이션으로 뺄거임
         */
        // 파일 이름 확인
        String originalFilename = profileImg.getOriginalFilename();

        if (originalFilename.length() > 50) throw new TooLongMemberProfileImgNameException();
        // 확장자 추출
        int dotIndex = originalFilename != null ? originalFilename.lastIndexOf('.') : -1;
        if (dotIndex == -1 || !List.of("png", "jpg", "jpeg").contains( originalFilename.substring(dotIndex + 1).toLowerCase()))
            throw new InvalidMemberProfileImgExtensionException();

        return ResponseEntity.ok(memberService.updateProfileImg(profileImg));
    }

}

