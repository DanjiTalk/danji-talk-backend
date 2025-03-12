package com.danjitalk.danjitalk.api.community.reaction;

import com.danjitalk.danjitalk.application.community.reaction.ReactionService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/{feedId}")
    public ResponseEntity<ApiResponse<Void>> incrReaction(@PathVariable Long feedId) {
        reactionService.incrReaction(feedId);
        return ResponseEntity.ok().body(ApiResponse.success(200, null, null));
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<ApiResponse<Void>> decrReaction(@PathVariable Long feedId) {
        reactionService.decrReaction(feedId);
        return ResponseEntity.ok().body(ApiResponse.success(200, null, null));
    }


}
