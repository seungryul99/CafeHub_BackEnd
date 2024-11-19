package com.cafehub.backend.domain.menu.dto.response;

import com.cafehub.backend.domain.menu.entity.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuListResponse {

    private List<MenuDetail> menuList;

    @Getter
    @Setter
    public static class MenuDetail {

        private Long menuId;
        private Category category;
        private String menuName;
        private Integer price;

        @QueryProjection
        public MenuDetail (Long menuId, Category category, String menuName, Integer price){
            this.menuId = menuId;
            this.category = category;
            this.menuName = menuName;
            this.price = price;
        }
    }
}
