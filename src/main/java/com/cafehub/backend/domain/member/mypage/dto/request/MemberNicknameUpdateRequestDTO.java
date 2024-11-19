package com.cafehub.backend.domain.member.mypage.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberNicknameUpdateRequestDTO {

    @Length(min = 3, max = 10, message = "닉네임은 3 ~ 10자여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s]*$", message = "닉네임에는 영문,한글,공백,숫자만 허용됩니다")
    private String nickname;
}
