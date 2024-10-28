package com.cafehub.backend.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllCommentGetResponseDTO {

    private List<CommentDetail> comments;

    private Boolean isLast;

    private Integer currentPage;



    @Getter
    @Setter
    @NoArgsConstructor
    public static class CommentDetail{

        private Long commentId;

        private String nickname;

        private String commentWriterProfile;

        private String commentContent;

        @JsonFormat(pattern = "yy.MM.dd hh.mm")
        private LocalDateTime commentDate;

        private Boolean commentManagement;


        @QueryProjection
        public CommentDetail(Long commentId, String nickname, String commentWriterProfile,
                             String commentContent, LocalDateTime commentDate){

            this.commentId = commentId;
            this.nickname = nickname;
            this.commentWriterProfile = commentWriterProfile;
            this.commentContent = commentContent;
            this.commentDate = commentDate;
        }

        public void updateCommentManagement(){
            commentManagement = true;
        }
    }

    public static AllCommentGetResponseDTO of(List<CommentDetail> comments,
                                       Boolean isLast, Integer currentPage){

        return AllCommentGetResponseDTO.builder()
                .comments(comments)
                .isLast(isLast)
                .currentPage(currentPage)
                .build();
    }

}
