package com.danjitalk.danjitalk.api.apartment.bookmark;

import com.danjitalk.danjitalk.application.bookmark.BookmarkService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.bookmark.entity.enums.BookmarkType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apartments/{apartmentId}/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBookmark(@PathVariable Long apartmentId) {
        bookmarkService.addBookmark(apartmentId, BookmarkType.APARTMENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, null, null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(@PathVariable Long apartmentId) {
        bookmarkService.deleteBookmark(apartmentId, BookmarkType.APARTMENT);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(204, null, null));
    }
}
