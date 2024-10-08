package com.cafehub.backend.domain.menu.repository;

import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.QCafeInfoResponseDTO_BestMenuDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cafehub.backend.domain.menu.entity.QMenu.menu;

public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    // [Refactor Point] QueryDsl Config로 빼야 할까? 이유 분석하고 결정
    public MenuRepositoryCustomImpl(EntityManager entityManager){
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CafeInfoResponseDTO.BestMenuDetail> getBestMenuList(Long cafeId) {

        return jpaQueryFactory
                .select(new QCafeInfoResponseDTO_BestMenuDetail(
                        menu.id,
                        menu.name,
                        menu.price
                ))
                .from(menu)
                .where(menu.cafe.id.eq(cafeId))
                .fetch();
    }
}
