package com.cafehub.backend.domain.menu.repository;

import com.cafehub.backend.domain.cafe.dto.response.CafeInfoResponseDTO;
import com.cafehub.backend.domain.menu.dto.response.MenuListResponse;

import java.util.List;

public interface MenuRepositoryCustom {

    List<CafeInfoResponseDTO.BestMenuDetail> findBestMenuList(Long cafeId);

    List<MenuListResponse.MenuDetail> findAllMenuList(Long cafeId);
}
