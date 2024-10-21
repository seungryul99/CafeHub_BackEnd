package com.cafehub.backend.domain.reviewPhoto.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
public class ReviewPhotoDetail {

    private Long reviewId;

    private String photoUrl;


    @QueryProjection
    public ReviewPhotoDetail(Long reviewId, String photoUrl){
        this.reviewId = reviewId;
        this.photoUrl = photoUrl;
    }
}
