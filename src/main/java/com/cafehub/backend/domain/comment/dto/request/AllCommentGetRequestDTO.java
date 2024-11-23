package com.cafehub.backend.domain.comment.dto.request;


import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AllCommentGetRequestDTO {

    private Long reviewId;

    private Integer currentPage;

    public static AllCommentGetRequestDTO of (Long reviewId, Integer currentPage){

        return AllCommentGetRequestDTO.builder()
                .reviewId(reviewId)
                .currentPage(currentPage)
                .build();
    }
}
