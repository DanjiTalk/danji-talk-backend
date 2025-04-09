package com.danjitalk.danjitalk.api.community.feed;

import com.danjitalk.danjitalk.application.community.feed.FeedService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.CreateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.UpdateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.CreateFeedResponseDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.FeedDetailResponseDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.FeedListDto;
import com.danjitalk.danjitalk.domain.community.feed.enums.FeedSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/feeds")
public class FeedController {

    private final FeedService feedService;

    /**
     * 피드 상세 조회 API
     * @param feedId 조회할 FeedID
     * @return FeedDetailResponseDto
     * */
    @GetMapping("/{feedId}")
    public ResponseEntity<ApiResponse<FeedDetailResponseDto>> getFeedDetail(@PathVariable Long feedId) {
        return ResponseEntity.ok(ApiResponse.success(200, null, feedService.getFeedDetail(feedId)));
    }

    /**
     * 피드 생성 API
     * @param requestDto 피드 생성 요청 데이터 (제목, 내용 등)
     * @param multipartFileList 업로드할 파일 목록 (선택사항)
     * @return 생성된 정보와 상태코드 반환
     * */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateFeedResponseDto>> createFeed(
            @RequestPart("requestDto") CreateFeedRequestDto requestDto,
            @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, null, feedService.save(requestDto, multipartFileList)));
    }

    /**
     * 피드 업데이트 API
     * @param feedId 업데이트할 피드 ID
     * @param requestDto 수정할 제목, 내용 DTO
     * @param multipartFileList 업로드할 파일 목록 (선택사항)
     * @param deleteFileUrls 삭제할 파일 목록 (선택사항)
     * */
    @PutMapping("/{feedId}")
    public ResponseEntity<ApiResponse<Void>> updateFeed(
            @PathVariable Long feedId,
            @RequestPart("requestDto") UpdateFeedRequestDto requestDto,
            @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
            @RequestPart(value = "deleteFileUrls", required = false) List<String> deleteFileUrls
    ) {
        feedService.updateFeed(feedId, requestDto, multipartFileList, deleteFileUrls);
        return ResponseEntity.ok(ApiResponse.success(200, null, null));
    }

    /**
     * 피드 삭제 API
     * @param feedId 삭제할 피드 ID
     * */
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ApiResponse<Void>> deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(204, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FeedListDto>> getFeedList(@RequestParam Long apartmentId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorDate, @RequestParam(required = false, defaultValue = "ALL") FeedSortType sort) {
        return ResponseEntity.ok(ApiResponse.success(200, null, feedService.getFeedList(apartmentId, cursorDate, sort)));
    }

}
