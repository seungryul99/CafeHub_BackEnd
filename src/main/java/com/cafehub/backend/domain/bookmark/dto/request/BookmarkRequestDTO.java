package com.cafehub.backend.domain.bookmark.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BookmarkRequestDTO {

    @NotNull(message = "cafeId는 Null일 수 없습니다")
    @Positive(message = "cafeId는 1 이상의 정수 값만 허용됩니다")
    private Long cafeId;

    @NotNull(message = "bookmarkChecked는 Null일 수 없습니다")
    private Boolean bookmarkChecked;
}
