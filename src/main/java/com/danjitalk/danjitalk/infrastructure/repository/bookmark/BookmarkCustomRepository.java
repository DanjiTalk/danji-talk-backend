package com.danjitalk.danjitalk.infrastructure.repository.bookmark;

import com.danjitalk.danjitalk.domain.bookmark.dto.IBookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import java.util.List;

public interface BookmarkCustomRepository {

    List<IBookmarkResponse> findByTypeAndMemberIdWithCursor(BookmarkType bookmarkType, Long memberId, Long cursor, Long limit);
}
