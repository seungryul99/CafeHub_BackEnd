package com.cafehub.backend.domain.authInfo.entity;

import com.cafehub.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    private String provider;
    private Long appId;

    @Builder
    private AuthInfo(String provider, Long appId) {
        this.provider = provider;
        this.appId = appId;
    }

    public static AuthInfo from(Long appId, String provider){

        return AuthInfo.builder()
                .appId(appId)
                .provider(provider)
                .build();
    }
}
