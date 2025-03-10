package com.danjitalk.danjitalk.application.community.comment;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.CreateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.UpdateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.community.comment.CommentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 작성
     * */
    @Transactional
    public void createComment(CreateCommentRequestDto createCommentRequestDto) {

        Feed feed = feedRepository.findById(createCommentRequestDto.feedId()).orElseThrow(() -> new DataNotFoundException());

        Comment parentComment = null;
        if (createCommentRequestDto.parentId() != null) {
            parentComment = commentRepository.findById(createCommentRequestDto.parentId()).orElse(null);
        }

        Member member = memberRepository.findById(SecurityContextHolderUtil.getMemberId()).orElseThrow(() -> new DataNotFoundException());

        Comment comment = Comment.builder()
                .contents(createCommentRequestDto.contents())
                .feed(feed)
                .parent(parentComment)
                .member(member)
                .build();

        commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     * */
    @Transactional
    public void updateComment(Long commentId, UpdateCommentRequestDto updateCommentRequestDto) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());

        Long memberId = SecurityContextHolderUtil.getMemberId();

        if(!comment.getMember().getId().equals(memberId)) {
            throw new BadRequestException("Not allowed to update comment.");
        }

        comment.changeContents(updateCommentRequestDto.contents());
    }

    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());

        Long memberId = SecurityContextHolderUtil.getMemberId();

        if(!comment.getMember().getId().equals(memberId)) {
            throw new BadRequestException("Not allowed to delete comment.");
        }

        commentRepository.delete(comment);
    }

}

