package com.cafehub.backend.common.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Column(name = "image_url")
    private String url;

    @Column(name = "s3_key")
    private String key;



    public Image(String url){
        
        this.url = url;
    }
}