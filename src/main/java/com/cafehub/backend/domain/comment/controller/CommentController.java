package com.cafehub.backend.domain.comment.controller;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.comment.dto.request.CommentCreateRequestDTO;
import com.cafehub.backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/auth/reviews/{reviewId}/comment")
    public ResponseEntity<ResponseDTO<Void>> writeComment(@PathVariable Long reviewId,
                                                          @RequestBody CommentCreateRequestDTO requestDTO){

        requestDTO.setReviewId(reviewId);

        return ResponseEntity.ok(commentService.writeComment(requestDTO));
    }
}
