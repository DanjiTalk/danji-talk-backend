package com.danjitalk.danjitalk.api.community.feed;

import com.danjitalk.danjitalk.application.community.feed.FeedService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.CreateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.CreateFeedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/feed")
public class FeedController {

    private final FeedService feedService;

    /**
     * 피드 생성 API
     * @param requestDto 피드 생성 요청 데이터 (제목, 내용 등)
     * @param multipartFileList 업로드할 파일 목록 (선택사항)
     * @return 생성된 정보와 상태코드 반환
     * */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CreateFeedResponseDto>> createFeed(
            @RequestPart("requestDto") CreateFeedRequestDto requestDto,
            @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, null, feedService.save(requestDto, multipartFileList)));
    }
}
