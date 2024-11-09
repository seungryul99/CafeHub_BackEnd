package com.cafehub.backend.domain.menu.service;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.menu.dto.response.MenuListResponse;
import com.cafehub.backend.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public ResponseDTO<MenuListResponse> getMenuList(Long cafeId) {

        return ResponseDTO.success(new MenuListResponse(menuRepository.findAllMenuList(cafeId)));
    }
}
