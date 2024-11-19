package com.cafehub.backend.domain.comment.repository;

import com.cafehub.backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> , CommentRepositoryCustom{
    List<Comment> findALlByMemberId(Long memberId);

    List<Comment> findAllByReviewId(Long reviewId);
}
