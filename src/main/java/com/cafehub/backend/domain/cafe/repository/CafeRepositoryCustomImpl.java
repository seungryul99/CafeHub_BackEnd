package com.cafehub.backend.domain.cafe.repository;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.dto.QCafeDetails;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.entity.Theme;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cafehub.backend.domain.cafe.entity.QCafe.cafe;



@Repository
public class CafeRepositoryCustomImpl implements CafeRepositoryCustom {

    private static final int PAGE_SIZE = 10;


    private final JPAQueryFactory jpaQueryFactory;

    // [Refactor Point] QueryDsl Config로 빼야 할까? 이유 분석하고 결정
    public CafeRepositoryCustomImpl(EntityManager entityManager){
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Slice<CafeDetails> findCafesBySlice(CafeListRequestDTO requestDTO) {

        int page = requestDTO.getCurrentPage();


        List<CafeDetails> cafeList = jpaQueryFactory
                .select(new QCafeDetails(
                        cafe.id,
                        cafe.cafeImg.url,
                        cafe.name,
                        cafe.theme,
                        cafe.rating,
                        cafe.reviewCnt))
                .from(cafe)
                .orderBy(getOrderSpecifier(requestDTO.getSortType()), cafe.id.desc())
                .where(themeEq(requestDTO.getTheme()))
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE + 1)
                .fetch();


        boolean hasNext = cafeList.size() > PAGE_SIZE;
        if (hasNext) cafeList.removeLast();

        return new SliceImpl<>(cafeList, PageRequest.of(page, PAGE_SIZE), hasNext);
    }


    private BooleanExpression themeEq(String theme) {
        if (theme.equals("All")) return null;
        return cafe.theme.eq(Theme.valueOf(theme));
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortType) {
        return switch (sortType) {
            case "rating" -> cafe.rating.desc();
            case "rating_a" -> cafe.rating.asc();
            case "reviewNum" -> cafe.reviewCnt.desc();
            case "reviewNum_a" -> cafe.reviewCnt.asc();
            case "name" -> cafe.name.asc();
            case "name_d" -> cafe.name.desc();
            default -> null;
        };
    }


}
