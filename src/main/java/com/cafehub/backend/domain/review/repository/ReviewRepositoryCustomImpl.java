package com.cafehub.backend.domain.review.repository;


import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.QCafeInfoResponseDTO_BestMenuDetail;
import com.cafehub.backend.domain.cafe.dto.response.QCafeInfoResponseDTO_BestReviewDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cafehub.backend.domain.review.entity.QReview.review;


public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // [Refactor Point] QueryDsl Config로 빼야 할까? 이유 분석하고 결정
    public ReviewRepositoryCustomImpl(EntityManager entityManager){
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    
    
    // 이렇게 TOP N개의 리뷰를 조회하기 위해서 모든 리뷰를 좋아요 순으로 정렬하고 
    // 위에서 부터 N개 가져오는 방식은 성능 문제를 발생 시킬 수 있기 때문에 이를 해결할 방법도 추후 생각해 봐야한다.
    @Override
    public List<CafeInfoResponseDTO.BestReviewDetail> findTopNReviewsByCafeId(Long cafeId, int topNReviewSize) {

        return jpaQueryFactory.select(new QCafeInfoResponseDTO_BestReviewDetail(
                        review.id,
                        review.writer,
                        review.rating,
                        review.content,
                        review.createdAt,
                        review.likeCnt,
                        review.commentCnt
                        ))
                .from(review)
                .where(review.cafe.id.eq(cafeId))
                .orderBy(review.likeCnt.desc(), review.commentCnt.desc())
                .limit(topNReviewSize)
                .fetch();
    }
}
