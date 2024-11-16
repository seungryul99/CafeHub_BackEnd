package com.cafehub.backend.domain.member.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Validated
@Tag(name = "1. [로그인]", description = "Login API")
public interface LoginControllerAPI {


    @Operation(
            summary = " OOO로 로그인하기 버튼 클릭",
            description = "사용자가 선택한 소셜 로그인을 처리합니다.",
            parameters = {
                    @Parameter(name = "provider", description = "kakao외의 값을 입력할 수 없습니다. 다른 소셜 로그인은 추후 추가해 나갈 계획", example = "kakao")
            })
    @GetMapping("/api/member/login/{provider}")
    ResponseEntity<Void> login(@Pattern(regexp = "^(kakao)$", message = "OAuth 로그인 provider가 잘못 입력 되었습니다") String provider);
}
