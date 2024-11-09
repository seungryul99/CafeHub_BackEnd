package com.cafehub.backend.domain.menu.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.menu.dto.response.MenuListResponse;
import com.cafehub.backend.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController implements MenuControllerAPI{

    private final MenuService menuService;

    @GetMapping("/cafe/{cafeId}/menu")
    public ResponseEntity<ResponseDTO<MenuListResponse>> getAllMenuOfCafe(@PathVariable Long cafeId){

        return ResponseEntity.ok(menuService.getMenuList(cafeId));
    }
}