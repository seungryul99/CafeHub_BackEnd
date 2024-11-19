package com.cafehub.backend.domain.menu.entity;

import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Boolean isBest;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;


    @Builder
    private Menu(Category category, String name, Integer price, Boolean isBest, Cafe cafe) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.isBest = isBest;
        this.cafe = cafe;
    }
}