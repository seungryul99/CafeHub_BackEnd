package com.cafehub.backend.common;


import jakarta.persistence.Embeddable;
import lombok.*;


/**
 *   S3 이미지 관련 복합값 타입, Getter만 열고 불변객체로 만든 후, 변경 사항이 있을시 새로운걸 만들어서 갈아 끼움
 */

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {


    private String url;

    private String key;
}