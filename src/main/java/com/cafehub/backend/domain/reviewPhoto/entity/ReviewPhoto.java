package com.cafehub.backend.domain.reviewPhoto.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import com.cafehub.backend.common.value.Image;
import com.cafehub.backend.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPhoto extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewPhoto_id")
    private Long id;

    @Embedded
    private Image reviewPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public ReviewPhoto(Image reviewPhoto, Review review) {
        this.reviewPhoto = reviewPhoto;
        this.review = review;
    }
}