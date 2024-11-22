package com.cafehub.backend.domain.comment.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.comment.dto.request.CommentCreateRequestDTO;
import com.cafehub.backend.domain.comment.dto.response.AllCommentGetResponseDTO;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Validated
public interface CommentControllerAPI {
    @GetMapping("/optional-auth/reviews/{reviewId}/comments/{currentPage}")
    ResponseEntity<ResponseDTO<AllCommentGetResponseDTO>> getAllComment(@Positive(message = "리뷰 아이디는 1이상의 정수여야 합니다") Long reviewId,
                                                                        @PositiveOrZero(message = "currentPage는 0 이상의 정수여야 합니다")Integer currentPage);


    @PostMapping("/auth/reviews/{reviewId}/comment")
    ResponseEntity<ResponseDTO<Void>> writeComment(@Positive(message = "리뷰 아이디는 1이상의 정수여야 합니다") Long reviewId,
                                                   CommentCreateRequestDTO requestDTO);


    @DeleteMapping("/auth/review/{reviewId}/comment/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(@Positive(message = "리뷰 아이디는 1이상의 정수여야 합니다") Long reviewId,
                                                           @Positive(message = "Comment 아이디는 1이상의 정수여야 합니다") Long commentId);
}
