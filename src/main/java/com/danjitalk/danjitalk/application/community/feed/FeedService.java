package com.danjitalk.danjitalk.application.community.feed;

import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.CreateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.CreateFeedResponseDto;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import com.danjitalk.danjitalk.infrastructure.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateFeedResponseDto save(CreateFeedRequestDto createFeedRequestDto, List<MultipartFile> multipartFileList) {

        // 제목, 피드타입이 없으면 Exception
        if(createFeedRequestDto.title() == null || createFeedRequestDto.feedType() == null) {
            throw new IllegalArgumentException("Title or FeedType cannot be null");
        }

        // 파일이 10개 이상이면 Exception
        if(multipartFileList != null && multipartFileList.size() > 10) {
            throw new IllegalArgumentException("More than 10 Files");
        }

        String fileUrl = null;

        // 파일이 있으면 S3에 업로드
        if(multipartFileList != null && !multipartFileList.isEmpty()) {
            String randomUUID = UUID.randomUUID().toString();
            fileUrl = s3Service.uploadFile(randomUUID, createFeedRequestDto.feedType(), multipartFileList);
        }

        Member member = memberRepository.findById(SecurityContextHolderUtil.getMemberId()).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Feed feed = new Feed(createFeedRequestDto.title(), createFeedRequestDto.contents(), createFeedRequestDto.feedType());
        feed.setMember(member);
        feed.setFileUrl(fileUrl);
        feedRepository.save(feed);

        return new CreateFeedResponseDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContents(),
                feed.getFeedType(),
                feed.getFileUrl()
        );
    }
}
