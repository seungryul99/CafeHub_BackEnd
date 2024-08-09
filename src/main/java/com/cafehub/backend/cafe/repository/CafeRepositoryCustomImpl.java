package com.cafehub.backend.cafe.repository;

import com.cafehub.backend.cafe.dto.CafeDetails;
import com.cafehub.backend.cafe.dto.QCafeDetails;
import com.cafehub.backend.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.cafe.entity.Theme;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.cafehub.backend.cafe.entity.QCafe.*;


public class CafeRepositoryCustomImpl implements CafeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public CafeRepositoryCustomImpl(EntityManager entityManager){
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }




    @Override
    public Slice<CafeDetails> findCafesBySlice(CafeListRequestDTO requestDTO) {

        int page = requestDTO.getCurrentPage();
        int pageSize = 10;


        List<CafeDetails> cafeList = jpaQueryFactory
                .select(new QCafeDetails(
                        cafe.id,
                        cafe.img.url,
                        cafe.name,
                        cafe.theme,
                        cafe.rating,
                        cafe.reviewCnt))
                .from(cafe)
                .orderBy(getOrderSpecifier(requestDTO.getSortType()), cafe.id.desc())
                .where(themeEq(requestDTO.getTheme()))
                .offset(page * pageSize)
                .limit(pageSize + 1)
                .fetch();


        boolean hasNext = cafeList.size() > pageSize;
        if (hasNext) cafeList.removeLast();

        return new SliceImpl<>(cafeList, PageRequest.of(page, pageSize), hasNext);
    }


    private BooleanExpression themeEq(String theme) {
        if (theme.equals("All")) return null;
        return cafe.theme.eq(Theme.valueOf(theme));
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortType) {
        return switch (sortType) {
            case "rating" -> cafe.rating.desc();
            case "reviewNum" -> cafe.reviewCnt.desc();
            case "name" -> cafe.name.asc();
            default ->null;
        };
    }

}
