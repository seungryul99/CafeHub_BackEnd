package com.cafehub.backend.cafe.entity;

import com.cafehub.backend.common.entity.BaseEntity;
import com.cafehub.backend.common.entity.Image;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cafe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Theme theme;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String operationHours;

    @Column(nullable = false)
    private String closeDays;

    @Embedded
    private Image img;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Integer reviewCnt;
}
