package com.cafehub.backend.common.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {

    @Column(name = "image_url")
    private String url;

    @Column(name = "s3_key")
    private String key;
}