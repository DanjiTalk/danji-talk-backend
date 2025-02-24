package com.danjitalk.danjitalk.application.community.feed;

import com.danjitalk.danjitalk.domain.community.feed.dto.request.CreateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.community.feed.service.FeedDomainService;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
import com.danjitalk.danjitalk.infrastructure.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedDomainService feedDomainService;
    private final S3Service s3Service;

    @Transactional
    public void save(CreateFeedRequestDto createFeedRequestDto) {
        if(createFeedRequestDto.title() == null || createFeedRequestDto.feedType() == null) {
            throw new IllegalArgumentException("Title or FeedType cannot be null");
        }

        s3Service.uploadFile(null, createFeedRequestDto.feedType(), createFeedRequestDto.multipartFileList());

        Feed feed = new Feed(createFeedRequestDto.title(), createFeedRequestDto.contents(), createFeedRequestDto.feedType());
        feedRepository.save(feed);

    }
}
