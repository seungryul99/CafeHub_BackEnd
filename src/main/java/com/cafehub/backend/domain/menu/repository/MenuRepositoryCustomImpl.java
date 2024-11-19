package com.cafehub.backend.domain.menu.repository;

import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.cafe.dto.response.QCafeInfoResponseDTO_BestMenuDetail;
import com.cafehub.backend.domain.menu.dto.response.MenuListResponse;
import com.cafehub.backend.domain.menu.dto.response.QMenuListResponse_MenuDetail;
import com.cafehub.backend.domain.menu.exception.MenuListNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cafehub.backend.domain.menu.entity.QMenu.menu;


@Repository
@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CafeInfoResponseDTO.BestMenuDetail> findBestMenuList(Long cafeId) {

        return jpaQueryFactory
                .select(new QCafeInfoResponseDTO_BestMenuDetail(
                        menu.id,
                        menu.name,
                        menu.price
                ))
                .from(menu)
                .where(menu.cafe.id.eq(cafeId))
                .where(menu.isBest.eq(true))
                .orderBy(menu.name.asc())
                .fetch();
    }

    @Override
    public List<MenuListResponse.MenuDetail> findAllMenuList(Long cafeId) {

        List<MenuListResponse.MenuDetail> menuDetailList = jpaQueryFactory
                .select(new QMenuListResponse_MenuDetail(
                        menu.id,
                        menu.category,
                        menu.name,
                        menu.price
                ))
                .from(menu)
                .where(menu.cafe.id.eq(cafeId))
                .fetch();

        if (menuDetailList.isEmpty()) throw new MenuListNotFoundException();

        return menuDetailList;
    }
}
