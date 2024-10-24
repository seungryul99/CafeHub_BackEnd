package com.cafehub.backend.common.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Column(name = "s3_key")
    private String key;

    @Column(name = "image_url", nullable = false)
    private String url;


    public Image(String url){
        this.url = url;
    }

    public void updateUrl(String key,String url){
        this.key = key;
        this.url = url;
    }
}