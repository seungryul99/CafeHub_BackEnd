package com.cafehub.backend.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class S3KeyGenerator {

    @Value("${spring.cloud.aws.s3.path.cafe}")
    private String cafePath;

    @Value("${spring.cloud.aws.s3.path.reviewPhoto}")
    private String reviewPhotoPath;

    @Value("${spring.cloud.aws.s3.path.memberProfileImg}")
    private  String memberProfileImgPath;


    public String generateReviewPhotoImgS3Key(String fileName){
        return reviewPhotoPath + '/' + fileName + UUID.randomUUID();
    }

    public String generateProfileImgS3Key(String fileName) {
        return memberProfileImgPath + '/' + fileName + UUID.randomUUID();
    }
}
