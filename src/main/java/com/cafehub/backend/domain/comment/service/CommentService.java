package com.cafehub.backend.domain.comment.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.comment.dto.request.CommentCreateRequestDTO;
import com.cafehub.backend.domain.comment.entity.Comment;
import com.cafehub.backend.domain.comment.repository.CommentRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final JwtThreadLocalStorage jwtThreadLocalStorage;

    private final ReviewRepository reviewRepository;

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;


    public ResponseDTO<Void> writeComment(CommentCreateRequestDTO requestDTO) {

        Member loginMember = memberRepository.findById(jwtThreadLocalStorage.getMemberIdFromJwt()).get();
        Review review = reviewRepository.findById(requestDTO.getReviewId()).get();

        Comment comment = Comment.builder()
                .content(requestDTO.getCommentContent())
                .writer(loginMember.getNickname())
                .member(loginMember)
                .review(review)
                .build();

        commentRepository.save(comment);

        review.updateCommentCntByAddComment();

        return ResponseDTO.success(null);
    }
}
