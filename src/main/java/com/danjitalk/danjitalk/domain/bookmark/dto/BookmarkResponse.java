package com.danjitalk.danjitalk.domain.bookmark.dto;

import java.util.List;

public record BookmarkResponse(
    List<IBookmarkResponse> bookmarkResponses,
    Boolean lastPage
) {

}
