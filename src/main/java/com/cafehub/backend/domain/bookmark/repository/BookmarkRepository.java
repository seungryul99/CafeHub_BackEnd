package com.cafehub.backend.domain.bookmark.repository;

import com.cafehub.backend.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
    Boolean existsByCafeIdAndMemberId(Long cafeId, Long id);


    void deleteByCafeIdAndMemberId(Long cafeId, Long memberId);

    List<Bookmark> findAllByMemberId(Long memberId);
}
