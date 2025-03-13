package com.danjitalk.danjitalk.infrastructure.repository.community.comment;

import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}
