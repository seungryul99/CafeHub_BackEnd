package com.cafehub.backend.domain.bookmark.repository;

import com.cafehub.backend.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Boolean existsByCafeIdAndMemberId(Long cafeId, Long id);

    Long findByCafeIdAndMemberId(Long cafeId, Long memberId);

    void deleteByCafeIdAndMemberId(Long cafeId, Long memberId);
}
