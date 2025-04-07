package com.danjitalk.danjitalk.application.community.bookmark;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.community.bookmark.entity.Bookmark;
import com.danjitalk.danjitalk.infrastructure.repository.community.bookmark.BookmarkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    /**
     * 북마크 체크 
     */
    public Boolean isBookmarked(Long feedId) {

        Long validFeedId = this.validatedFeedId(feedId);
        Long memberId = SecurityContextHolderUtil.getMemberId();

        return bookmarkRepository.existsByFeedIdAndMemberId(validFeedId, memberId);

    }
    
    /**
     * 북마크 추가
     * */
    @Transactional
    public void addBookmark(Long feedId) {

        Long validFeedId = this.validatedFeedId(feedId);

        Long memberId = SecurityContextHolderUtil.getMemberId();

        boolean exists = bookmarkRepository.existsByFeedIdAndMemberId(feedId, memberId);

        if(exists) {
            throw new IllegalArgumentException("Bookmark already exists");
        }

        Bookmark bookmark = new Bookmark(memberId, feedId);

        bookmarkRepository.save(bookmark);

    }
    
    /**
     * 북마크 제거
     * */
    @Transactional
    public void deleteBookmark(Long feedId) {
        Long validFeedId = this.validatedFeedId(feedId);
        Long memberId = SecurityContextHolderUtil.getMemberId();

        boolean exists = bookmarkRepository.existsByFeedIdAndMemberId(validFeedId, memberId);

        if(!exists) {
            throw new IllegalArgumentException("Bookmark does not exist");
        }

        bookmarkRepository.deleteByFeedIdAndMemberId(validFeedId, memberId);
    }

    /**
     * feedId 체크
     * */
    private Long validatedFeedId(Long feedId) {
        if(feedId == null) {
            throw new BadRequestException("FeedId must not be null");
        }

        return feedId;
    }
    
}
