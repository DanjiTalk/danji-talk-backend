package com.danjitalk.danjitalk.api.community.comment;

import com.danjitalk.danjitalk.application.community.comment.CommentService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.CreateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.UpdateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.response.GetCommentResponseDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{feedId}")
    public ResponseEntity<ApiResponse<PageResponseDto<GetCommentResponseDto>>> getComments(@PathVariable Long feedId, @RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok().body(ApiResponse.success(200, null, commentService.getComments(feedId, page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(@RequestBody CreateCommentRequestDto createCommentRequestDto) {
        commentService.createComment(createCommentRequestDto);
        return ResponseEntity.ok().body(ApiResponse.success(200, null, null));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequestDto updateCommentRequestDto) {
        commentService.updateComment(commentId, updateCommentRequestDto);
        return ResponseEntity.ok().body(ApiResponse.success(200, null, null));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body(ApiResponse.success(200, null, null));
    }

}
