package com.danjitalk.danjitalk.application.bookmark;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.bookmark.dto.BookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.dto.IBookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.entity.Bookmark;
import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import com.danjitalk.danjitalk.infrastructure.repository.bookmark.BookmarkRepository;
import jakarta.transaction.Transactional;
import java.util.List;
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

    public BookmarkResponse getBookmarks(BookmarkType bookmarkType, Long cursor, long limit) {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        // 북마크에 필요한 자료
        // A Type: 아파트 정보(id, 이름, 지역, 아파트 세대, 동 수, 아파트 썸네일, 북마크 여부)
        // B Type: 피드 정보(id, 게시글 제목, 내용 썸네일, 작성자?, 작성일 등)
        List<IBookmarkResponse> bookmarkResponses = bookmarkRepository.findByTypeAndMemberIdWithCursor(bookmarkType, memberId, cursor, limit + 1);
        boolean lastPage = bookmarkResponses.size() <= limit; // true => 이후 데이터 없음
        if (!lastPage) {
            bookmarkResponses.remove((int) limit); // 초과분 제거
        }

        return new BookmarkResponse(bookmarkResponses, lastPage);
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
