package com.cafehub.backend.domain.comment.repository;


import com.cafehub.backend.domain.comment.dto.request.AllCommentGetRequestDTO;
import com.cafehub.backend.domain.comment.dto.response.AllCommentGetResponseDTO;
import com.cafehub.backend.domain.comment.dto.response.QAllCommentGetResponseDTO_CommentDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cafehub.backend.domain.comment.entity.QComment.comment;
import static com.cafehub.backend.domain.member.entity.QMember.member;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private static final int COMMENT_PAGE_SIZE = 10;

    private final JPAQueryFactory jpaQueryFactory;

    // [Refactor Point] QueryDsl Config로 빼야 할까? 이유 분석하고 결정
    public CommentRepositoryCustomImpl(EntityManager entityManager){
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Slice<AllCommentGetResponseDTO.CommentDetail> findCommentsBySlice(AllCommentGetRequestDTO requestDTO) {

        int page = requestDTO.getCurrentPage();


        List<AllCommentGetResponseDTO.CommentDetail> commentDetails = jpaQueryFactory
                .select(new QAllCommentGetResponseDTO_CommentDetail(
                        comment.id,
                        comment.writer,
                        member.profileImg.url,
                        comment.content,
                        comment.createdAt
                        ))
                .from(comment)
                .join(comment.member, member)
                .orderBy(comment.createdAt.desc())
                .where(comment.review.id.eq(requestDTO.getReviewId()))
                .offset(page * COMMENT_PAGE_SIZE)
                .limit(COMMENT_PAGE_SIZE + 1)
                .fetch();


        boolean hasNext = commentDetails.size() > COMMENT_PAGE_SIZE;
        if (hasNext) commentDetails.removeLast();

        return new SliceImpl<>(commentDetails, PageRequest.of(page, COMMENT_PAGE_SIZE), hasNext);
    }
}
