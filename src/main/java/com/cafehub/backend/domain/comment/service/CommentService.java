package com.cafehub.backend.domain.comment.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.filter.jwt.JwtThreadLocalStorage;
import com.cafehub.backend.domain.comment.dto.request.AllCommentGetRequestDTO;
import com.cafehub.backend.domain.comment.dto.request.CommentCreateRequestDTO;
import com.cafehub.backend.domain.comment.dto.response.AllCommentGetResponseDTO;
import com.cafehub.backend.domain.comment.entity.Comment;
import com.cafehub.backend.domain.comment.repository.CommentRepository;
import com.cafehub.backend.domain.member.entity.Member;
import com.cafehub.backend.domain.member.mypage.repository.MemberRepository;
import com.cafehub.backend.domain.review.entity.Review;
import com.cafehub.backend.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Transactional(readOnly = true)
    public ResponseDTO<AllCommentGetResponseDTO> getAllCommentsBySlice(AllCommentGetRequestDTO requestDTO) {

        Slice<AllCommentGetResponseDTO.CommentDetail> commentDetails = commentRepository.findCommentsBySlice(requestDTO);
        Member member = memberRepository.findById(jwtThreadLocalStorage.getMemberIdFromJwt()).get();

        updateCommentManagement(commentDetails.getContent(), member.getNickname());

        return ResponseDTO.success(AllCommentGetResponseDTO.of(commentDetails.getContent(), commentDetails.isLast(), commentDetails.getNumber()));
    }

    private void updateCommentManagement(List<AllCommentGetResponseDTO.CommentDetail> comments, String nickname){

        for(AllCommentGetResponseDTO.CommentDetail cd : comments){

            if (cd.getNickname().equals(nickname)) cd.updateCommentManagement();
        }
    }
}
