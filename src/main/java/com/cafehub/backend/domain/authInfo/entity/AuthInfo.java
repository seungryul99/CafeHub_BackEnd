package com.cafehub.backend.domain.authInfo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    private String provider;

    private String jwtRefreshToken;

    private Date jwtRefreshTokenExpireIn;

    private Long appId;

    public void updateAuthInfo(String jwtRefreshToken, Date expiration) {

        this.jwtRefreshToken = jwtRefreshToken;
        this.jwtRefreshTokenExpireIn = expiration;
    }
}
