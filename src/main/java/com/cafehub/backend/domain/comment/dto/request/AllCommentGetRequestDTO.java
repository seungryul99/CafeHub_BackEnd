package com.cafehub.backend.domain.comment.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllCommentGetRequestDTO {

    private Long reviewId;

    private Integer currentPage;
}
