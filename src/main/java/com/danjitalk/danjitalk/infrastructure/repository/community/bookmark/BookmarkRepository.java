package com.danjitalk.danjitalk.infrastructure.repository.community.bookmark;

import com.danjitalk.danjitalk.domain.community.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByFeedIdAndMemberId(Long feedId, Long memberId);

    void deleteByFeedIdAndMemberId(Long feedId, Long memberId);
}
