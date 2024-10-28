package com.cafehub.backend.domain.comment.repository;

import com.cafehub.backend.domain.comment.dto.request.AllCommentGetRequestDTO;
import com.cafehub.backend.domain.comment.dto.response.AllCommentGetResponseDTO;
import org.springframework.data.domain.Slice;

public interface CommentRepositoryCustom {
    Slice<AllCommentGetResponseDTO.CommentDetail> findCommentsBySlice(AllCommentGetRequestDTO requestDTO);
}
