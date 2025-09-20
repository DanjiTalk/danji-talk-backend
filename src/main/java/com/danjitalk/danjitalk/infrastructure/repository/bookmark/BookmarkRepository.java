package com.danjitalk.danjitalk.infrastructure.repository.bookmark;

import com.danjitalk.danjitalk.domain.bookmark.entity.Bookmark;
import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkCustomRepository {

    boolean existsByTypeIdAndTypeAndMemberId(Long typeId, BookmarkType type, Long memberId);

    void deleteByTypeIdAndTypeAndMemberId(Long typeId, BookmarkType type, Long memberId);
    List<Bookmark> findByMemberId(Long memberId);
}
