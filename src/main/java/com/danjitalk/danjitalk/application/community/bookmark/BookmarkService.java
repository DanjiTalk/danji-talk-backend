package com.danjitalk.danjitalk.application.community.bookmark;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.community.bookmark.dto.request.BookmarkRequestDto;
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
    public void addBookmark(BookmarkRequestDto bookmarkRequestDto) {

        Long feedId = this.validatedFeedId(bookmarkRequestDto.feedId());
        Long memberId = SecurityContextHolderUtil.getMemberId();

        boolean exists = bookmarkRepository.existsByFeedIdAndMemberId(feedId, memberId);

        if(exists) {
            throw new IllegalArgumentException("Bookmark already exists");
        }

        bookmarkRepository.save(BookmarkRequestDto.toEntity(null, feedId, memberId));

    }
    
    /**
     * 북마크 제거
     * */
    @Transactional
    public void deleteBookmark(BookmarkRequestDto bookmarkRequestDto) {
        Long feedId = this.validatedFeedId(bookmarkRequestDto.feedId());
        Long memberId = SecurityContextHolderUtil.getMemberId();

        boolean exists = bookmarkRepository.existsByFeedIdAndMemberId(feedId, memberId);

        if(!exists) {
            throw new IllegalArgumentException("Bookmark does not exist");
        }

        bookmarkRepository.deleteByFeedIdAndMemberId(feedId, memberId);
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
