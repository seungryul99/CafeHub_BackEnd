package com.cafehub.backend.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDTO {

    @NotBlank(message = "댓글 내용은 공백으로만 이루어 질 수 없습니다")
    @Length(min = 1, max = 500, message = "댓글은 1~500자 까지 허용됩니다")
    private String commentContent;

}
