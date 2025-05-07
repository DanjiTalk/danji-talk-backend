package com.danjitalk.danjitalk.api.bookmark;

import com.danjitalk.danjitalk.application.bookmark.BookmarkService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.bookmark.dto.BookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/feeds/{feedId}")
    public ResponseEntity<ApiResponse<Void>> createFeedBookmark(@PathVariable Long feedId) {
        bookmarkService.addBookmark(feedId, BookmarkType.FEED);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, null, null));
    }

    @GetMapping("/feeds")
    public ResponseEntity<ApiResponse<BookmarkResponse>> getFeedBookmarks(
        @RequestParam(required = false) Long lastIndex,
        @RequestParam(required = false, defaultValue = "20") long limit
    ) {
        BookmarkResponse bookmarkResponse = bookmarkService.getBookmarks(BookmarkType.FEED, lastIndex, limit);
        return ResponseEntity.ok(ApiResponse.success(200, null, bookmarkResponse));
    }

    @DeleteMapping("/feeds/{feedId}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedBookmark(@PathVariable Long feedId) {
        bookmarkService.deleteBookmark(feedId, BookmarkType.FEED);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(204, null, null));
    }

    @PostMapping("/apartments/{apartmentId}")
    public ResponseEntity<ApiResponse<Void>> createApartmentBookmark(@PathVariable Long apartmentId) {
        bookmarkService.addBookmark(apartmentId, BookmarkType.APARTMENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, null, null));
    }

    @GetMapping("/apartments")
    public ResponseEntity<ApiResponse<BookmarkResponse>> getApartmentBookmarks(
        @RequestParam(required = false) Long lastIndex,
        @RequestParam(required = false, defaultValue = "20") long limit
    ) {
        BookmarkResponse bookmarkResponse = bookmarkService.getBookmarks(BookmarkType.APARTMENT, lastIndex, limit);
        return ResponseEntity.ok(ApiResponse.success(200, null, bookmarkResponse));
    }

    @DeleteMapping("/apartments/{apartmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteApartmentBookmark(@PathVariable Long apartmentId) {
        bookmarkService.deleteBookmark(apartmentId, BookmarkType.APARTMENT);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(204, null, null));
    }
}
