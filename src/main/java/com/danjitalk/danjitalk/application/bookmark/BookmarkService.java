package com.danjitalk.danjitalk.application.bookmark;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.bookmark.entity.Bookmark;
import com.danjitalk.danjitalk.domain.bookmark.entity.enums.BookmarkType;
import com.danjitalk.danjitalk.infrastructure.repository.community.bookmark.BookmarkRepository;
import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
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
    public Boolean isBookmarked(Long typeId, BookmarkType bookmarkType) {

        Long validTypeId = this.validatedTypeId(typeId, bookmarkType);
        Long memberId = SecurityContextHolderUtil.getMemberIdOptional().orElse(0L);

        return bookmarkRepository.existsByTypeIdAndTypeAndMemberId(validTypeId, bookmarkType, memberId);

    }
    
    /**
     * 북마크 추가
     * */
    @Transactional
    public void addBookmark(Long typeId, BookmarkType bookmarkType) {
        this.validatedTypeId(typeId, bookmarkType);

        Long memberId = SecurityContextHolderUtil.getMemberId();

        boolean exists = bookmarkRepository.existsByTypeIdAndTypeAndMemberId(typeId, bookmarkType, memberId);

        if(exists) {
            throw new IllegalArgumentException("Bookmark already exists");
        }

        Bookmark bookmark = new Bookmark(memberId, typeId, bookmarkType);

        bookmarkRepository.save(bookmark);

    }
    
    /**
     * 북마크 제거
     * */
    @Transactional
    public void deleteBookmark(Long typeId, BookmarkType bookmarkType) {
        this.validatedTypeId(typeId, bookmarkType);

        Long memberId = SecurityContextHolderUtil.getMemberId();

        boolean exists = bookmarkRepository.existsByTypeIdAndTypeAndMemberId(typeId, bookmarkType, memberId);

        if(!exists) {
            throw new IllegalArgumentException("Bookmark does not exist");
        }

        bookmarkRepository.deleteByTypeIdAndTypeAndMemberId(typeId, bookmarkType, memberId);
    }

    /**
     * typeId 체크
     */
    private Long validatedTypeId(Long typeId, BookmarkType bookmarkType) {
        if (typeId == null) {
            if (bookmarkType == BookmarkType.FEED) {
                throw new BadRequestException("FeedId must not be null");
            }

            if (bookmarkType == BookmarkType.APARTMENT) {
                throw new BadRequestException("apartmentId must not be null");
            }
        }
        return typeId;
    }
    
}
