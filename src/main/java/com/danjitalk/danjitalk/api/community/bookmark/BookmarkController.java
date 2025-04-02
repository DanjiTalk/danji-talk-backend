package com.danjitalk.danjitalk.api.community.bookmark;

import com.danjitalk.danjitalk.application.community.bookmark.BookmarkService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/feeds/{feedId}/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBookmark(@PathVariable Long feedId) {
        bookmarkService.addBookmark(feedId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, null, null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(@PathVariable Long feedId) {
        bookmarkService.deleteBookmark(feedId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(204, null, null));
    }

}
