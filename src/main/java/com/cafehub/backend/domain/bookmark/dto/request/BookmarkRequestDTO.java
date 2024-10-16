package com.cafehub.backend.domain.bookmark.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BookmarkRequestDTO {

    private Long cafeId;

    private Boolean bookmarkChecked;
}
