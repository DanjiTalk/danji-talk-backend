package com.danjitalk.danjitalk.api.community.comment;

import com.danjitalk.danjitalk.application.community.comment.CommentService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.CreateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.request.UpdateCommentRequestDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.response.GetCommentResponseDto;
import com.danjitalk.danjitalk.domain.community.comment.dto.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/feeds/{feedId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDto<GetCommentResponseDto>>> getComments(@PathVariable Long feedId, @RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok().body(ApiResponse.success(200, null, commentService.getComments(feedId, page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(@PathVariable Long feedId, @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        commentService.createComment(feedId, createCommentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "comment created successfully", null));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long feedId, @PathVariable Long commentId, @RequestBody UpdateCommentRequestDto updateCommentRequestDto) {
        commentService.updateComment(feedId, commentId, updateCommentRequestDto);
        return ResponseEntity.ok().body(ApiResponse.success(200, null, null));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long feedId, @PathVariable Long commentId) {
        commentService.deleteComment(feedId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(204, null, null));
    }

}
