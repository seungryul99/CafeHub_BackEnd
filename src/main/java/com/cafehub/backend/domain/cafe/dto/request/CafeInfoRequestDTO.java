package com.cafehub.backend.domain.cafe.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CafeInfoRequestDTO {

    private Long cafeId;

    private String jwtAccessToken;
}
