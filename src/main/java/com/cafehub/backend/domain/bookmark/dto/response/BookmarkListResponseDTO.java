package com.cafehub.backend.domain.bookmark.dto.response;

import com.cafehub.backend.domain.cafe.dto.CafeDetails;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BookmarkListResponseDTO {

    List<CafeDetails> cafeList;
}