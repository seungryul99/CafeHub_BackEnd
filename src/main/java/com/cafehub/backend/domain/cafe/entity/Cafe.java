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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    @Builder
    private Cafe(String name, Theme theme, String address, String phone,
                String operationHours, String closeDays, Image cafeImg,
                Double rating, Integer reviewCnt, List<Menu> menuList) {

        this.name = name;
        this.theme = theme;
        this.address = address;
        this.phone = phone;
        this.operationHours = operationHours;
        this.closeDays = closeDays;
        this.cafeImg = cafeImg;
        this.rating = rating;
        this.reviewCnt = reviewCnt;
        this.menuList = menuList;
    }

    public void updateRatingAndReviewCountByAddReview(Integer rating){

        if(this.reviewCnt == 0) {
            this.rating = rating.doubleValue();
            reviewCnt++;
            return;
        }

        double sum = this.rating * reviewCnt + rating;
        reviewCnt++;

        this.rating = sum/reviewCnt;
    }

    public void updateRatingAndReviewCountByDeleteReview(Integer rating){

        if(this.reviewCnt == 1){
            reviewCnt--;
            this.rating = 0.0;
            return;
        }

        double sum = this.rating * reviewCnt -rating;
        reviewCnt--;

        this.rating = sum/reviewCnt;
    }
}
