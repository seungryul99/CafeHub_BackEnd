package com.cafehub.backend.review.entity;

import com.cafehub.backend.cafe.entity.Cafe;
import com.cafehub.backend.common.entity.BaseEntity;
import com.cafehub.backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

/**
 *   한 리뷰의 사진은 5장 까지로 제한을 둘 예정이고 리뷰의 사진들은 Json 형식의 문자열로
 *   DB에 직접 저장 해서 사용
 */

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Integer likeCnt;

    @Column(nullable = false)
    private Integer commentCnt;

    @Column(nullable = false)
    private String writer;

    @Lob
    private String reviewPhotos;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
}
