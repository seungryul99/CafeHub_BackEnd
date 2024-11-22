package com.cafehub.backend.domain.comment.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.comment.dto.request.AllCommentGetRequestDTO;
import com.cafehub.backend.domain.comment.dto.request.CommentCreateRequestDTO;
import com.cafehub.backend.domain.comment.dto.response.AllCommentGetResponseDTO;
import com.cafehub.backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController implements CommentControllerAPI{

    private final CommentService commentService;

    @GetMapping("/optional-auth/reviews/{reviewId}/comments/{currentPage}")
    public ResponseEntity<ResponseDTO<AllCommentGetResponseDTO>> getAllComment(@PathVariable Long reviewId,
                                                                               @PathVariable Integer currentPage){

        return ResponseEntity.ok(commentService.getAllCommentsBySlice(AllCommentGetRequestDTO.of(reviewId, currentPage)));
    }


    @PostMapping("/auth/reviews/{reviewId}/comment")
    public ResponseEntity<ResponseDTO<Void>> writeComment(@PathVariable Long reviewId,
                                                          @RequestBody @Validated CommentCreateRequestDTO requestDTO){

        return ResponseEntity.ok(commentService.writeComment(reviewId, requestDTO));
    }


    @DeleteMapping("/auth/review/{reviewId}/comment/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(@PathVariable Long reviewId,
                                                           @PathVariable Long commentId){

        return ResponseEntity.ok(commentService.deleteComment(reviewId,commentId));
    }
}
