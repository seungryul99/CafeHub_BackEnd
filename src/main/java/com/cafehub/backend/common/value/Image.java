package com.cafehub.backend.common.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Column(length = 500, name = "s3_key")
    private String key;

    @Column(name = "image_url", columnDefinition = "VARCHAR(500) DEFAULT 'DefaultImage'")
    private String url;


    public Image(String url){
        this.url = url;
    }


    // 값타입 객체는 여러곳에서 공유해서 사용할 수 있기때문에 값에 대한 수정이 일어나면 안됨
    // 하지만 사진 변경을 위해서 값이 변경됨. 이를 위해서는 필드의 값을 변경할게 아니라
    // 새로운 Image 객체를 만들어서 변경된 값을 받아서 넘겨줘야 함.
    // 즉, 이건 잘못된 구현임
    public void updateUrl(String key,String url){
        this.key = key;
        this.url = url;
    }
}