package com.cafehub.backend.domain.comment.repository;

import com.cafehub.backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
