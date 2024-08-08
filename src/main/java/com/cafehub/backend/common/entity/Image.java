package com.cafehub.backend.common.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;




@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {

    private String url;

    @Column (name = "S3_key")
    private String key;
}