package com.cafehub.backend.domain.menu.controller;

import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.domain.menu.dto.response.MenuListResponse;
import com.cafehub.backend.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;


    @GetMapping("/cafe/{cafeId}/menu")
    public ResponseEntity<ResponseDTO<MenuListResponse>> getMenuListOfCafe(@PathVariable Long cafeId){

        return ResponseEntity.ok(menuService.getMenuList(cafeId));
    }

}