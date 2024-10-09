package com.cafehub.backend.domain.bookmark.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BookmarkResponseDTO {

    private Long cafeId;
}