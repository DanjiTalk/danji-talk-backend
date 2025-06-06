package com.danjitalk.danjitalk.domain.bookmark.entity;

import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 해당 엔티티는 독립적으로 관리, 논리적 관계만 갖고 연관관계는 X
 * */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "feed_id", "type"}))
public class Bookmark extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long typeId;

    @Enumerated(EnumType.STRING)
    private BookmarkType type;

    @Builder
    public Bookmark(Long memberId, Long typeId, BookmarkType type) {
        this.id = null;
        this.memberId = memberId;
        this.typeId = typeId;
        this.type = type;
    }
}
