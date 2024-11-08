package com.cafehub.backend.domain.cafe.repository;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import com.cafehub.backend.domain.cafe.dto.QCafeDetails;
import com.cafehub.backend.domain.cafe.dto.request.CafeListRequestDTO;
import com.cafehub.backend.domain.cafe.entity.Theme;
import com.cafehub.backend.domain.cafe.exception.InvalidCafeListPageRequestException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cafehub.backend.common.constants.CafeHubConstants.CAFE_LIST_PAGING_SIZE;
import static com.cafehub.backend.domain.cafe.entity.QCafe.cafe;


@Repository
@RequiredArgsConstructor
public class CafeRepositoryCustomImpl implements CafeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<CafeDetails> findCafesBySlice(CafeListRequestDTO requestDTO) {

        int currentPage = requestDTO.getCurrentPage();

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
                .offset(currentPage * CAFE_LIST_PAGING_SIZE)
                .limit(CAFE_LIST_PAGING_SIZE + 1)
                .fetch();

        if(cafeList.isEmpty()) throw new InvalidCafeListPageRequestException();

        boolean hasNext = cafeList.size() > CAFE_LIST_PAGING_SIZE;
        if (hasNext) cafeList.removeLast();

        return new SliceImpl<>(cafeList, PageRequest.of(currentPage, CAFE_LIST_PAGING_SIZE), hasNext);
    }

    private BooleanExpression themeEq(String theme) {
        if (theme.equals("All")) return null;
        return cafe.theme.eq(Theme.valueOf(theme));
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortType) {
        return switch (sortType) {
            case "rating_desc" -> cafe.rating.desc();
            case "rating_asc" -> cafe.rating.asc();
            case "reviewNum_desc" -> cafe.reviewCnt.desc();
            case "reviewNum_asc" -> cafe.reviewCnt.asc();
            case "name_asc" -> cafe.name.asc();
            case "name_desc" -> cafe.name.desc();
            default -> null;
        };
    }


    @Override
    public List<CafeDetails> findCafesByBookmarkList(List<Long> cafeIds) {

        List<CafeDetails> cafeList = jpaQueryFactory
                .select(new QCafeDetails(
                        cafe.id,
                        cafe.cafeImg.url,
                        cafe.name,
                        cafe.theme,
                        cafe.rating,
                        cafe.reviewCnt))
                .from(cafe)
                .where(cafe.id.in(cafeIds))
                .fetch();

        return cafeList;
    }


}
