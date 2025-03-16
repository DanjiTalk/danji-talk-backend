package com.danjitalk.danjitalk.infrastructure.repository.community.comment;

import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

    Page<Comment> findAllByFeedId(Long feedId, Pageable pageRequest);
}
