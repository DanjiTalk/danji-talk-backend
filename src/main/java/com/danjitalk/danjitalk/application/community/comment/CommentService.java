package com.danjitalk.danjitalk.application.community.comment;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.CreateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.UpdateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.response.GetCommentResponseDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.response.PageResponseDto;
import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.user.member.dto.response.CommentMemberResponseDto;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.community.comment.CommentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 조회 재귀 호출
     * */
    public PageResponseDto<GetCommentResponseDto> getComments(Long feedId, Integer page, Integer size) {

        Pageable pageRequest = PageRequest.of(page, size);

        Page<Comment> pageComment = commentRepository.findAllByFeedId(feedId, pageRequest);

        // 부모 댓글만 조회 후 -> 대댓글 조회
        List<GetCommentResponseDto> list = pageComment.stream()
                .map(comment -> this.converToDto(comment))
                .toList();

        return new PageResponseDto<GetCommentResponseDto>(
                list,
                pageComment.getNumber(),
                pageComment.getSize(),
                pageComment.getTotalElements(),
                pageComment.getTotalPages()
        );
    }

    /**
     * 댓글 작성
     * */
    @Transactional
    public void createComment(Long feedId, CreateCommentRequestDto createCommentRequestDto) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new DataNotFoundException());

        Comment parentComment = null;
        if (createCommentRequestDto.parentId() != null) {
            parentComment = commentRepository.findById(createCommentRequestDto.parentId()).orElse(null);
        }

        Member member = memberRepository.findById(SecurityContextHolderUtil.getMemberId()).orElseThrow(() -> new DataNotFoundException());

        Comment comment = Comment.createComment(createCommentRequestDto.contents(), parentComment, feed, member);
        commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     * */
    @Transactional
    public void updateComment(Long feedId, Long commentId, UpdateCommentRequestDto updateCommentRequestDto) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());

        if(!feedId.equals(comment.getFeed().getId())) {
            throw new BadRequestException("Not match comment id.");
        }

        Long memberId = SecurityContextHolderUtil.getMemberId();

        if(!comment.getMember().getId().equals(memberId)) {
            throw new BadRequestException("Not allowed to update comment.");
        }

        comment.changeContents(updateCommentRequestDto.contents());
    }

    @Transactional
    public void deleteComment(Long feedId, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());

        if(!feedId.equals(comment.getFeed().getId())) {
            throw new BadRequestException("Not match comment id.");
        }

        Long memberId = SecurityContextHolderUtil.getMemberId();

        if(!comment.getMember().getId().equals(memberId)) {
            throw new BadRequestException("Not allowed to delete comment.");
        }

        commentRepository.delete(comment);
    }


    /**
     * 재귀 호출로 DTO 변환
     * */
    private GetCommentResponseDto converToDto(Comment comment) {
        return new GetCommentResponseDto(
                comment.getId(),
                comment.getFeed().getId(),
                comment.getContents(),
                comment.getCreatedAt(),
                new CommentMemberResponseDto(comment.getMember().getId(), comment.getMember().getNickname(), comment.getMember().getFileId()),
                comment.getChildrenList().stream()
                        .map(this::converToDto)
                        .toList()
        );
    }

}

