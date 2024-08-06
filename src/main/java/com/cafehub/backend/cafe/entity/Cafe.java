package com.cafehub.backend.cafe.entity;

import com.cafehub.backend.common.BaseEntity;
import com.cafehub.backend.common.Image;
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
    private Theme theme;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String operationHours;

    @Column(columnDefinition = "varchar(100) default '휴무일 없음'")
    private String closeDays;

    @Embedded
    private Image img;

    @Column(nullable = false, columnDefinition = "double default 0")
    private Double rating;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer reviewCnt;
}
