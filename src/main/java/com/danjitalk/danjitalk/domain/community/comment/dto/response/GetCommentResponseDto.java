package com.danjitalk.danjitalk.domain.community.comment.dto.response;

import com.danjitalk.danjitalk.domain.user.member.dto.response.CommentMemberResponseDto;

import java.util.List;

public record GetCommentResponseDto(
        Long commentId,
        Long feedId,
        String contents,
        CommentMemberResponseDto commentMemberResponseDto,
        List<GetCommentResponseDto> childrenCommentDto
) {
}
