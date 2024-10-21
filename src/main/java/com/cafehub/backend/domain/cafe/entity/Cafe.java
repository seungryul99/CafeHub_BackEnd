package com.cafehub.backend.domain.cafe.entity;


import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cafe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Theme theme;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String operationHours;

    @Column(nullable = false)
    private String closeDays;

    @Embedded
    private Image cafeImg;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Integer reviewCnt;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();
}
