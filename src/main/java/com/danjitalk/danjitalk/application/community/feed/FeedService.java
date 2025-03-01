package com.danjitalk.danjitalk.application.community.feed;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.CreateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.UpdateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.CreateFeedResponseDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.FeedDetailResponseDto;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.s3.dto.response.S3ObjectResponseDto;
import com.danjitalk.danjitalk.domain.user.member.dto.response.FeedMemberResponseDto;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import com.danjitalk.danjitalk.infrastructure.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;

    /**
     * 피드 상세 조회
     *
     * */
    public FeedDetailResponseDto getFeedDetail(Long feedId) {

        if(feedId == null) {
            throw new IllegalArgumentException("FeedId cannot be null");
        }

        Feed feed = feedRepository.findFeedFetchJoinMemberByFeedId((feedId)).orElseThrow(DataNotFoundException::new);

        List<S3ObjectResponseDto> s3ObjectResponseDtoList = Optional.ofNullable(feed.getFileUrl()).map(url -> s3Service.getS3Object(url)).orElseGet(Collections::emptyList);

        return new FeedDetailResponseDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContents(),
                feed.getCreatedAt(),
                new FeedMemberResponseDto(
                        feed.getMember().getId(),
                        feed.getMember().getNickname()
                ),
                s3ObjectResponseDtoList
        );
    }

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

    @Transactional
    public void updateFeed(Long feedId, UpdateFeedRequestDto updateFeedRequestDto, List<MultipartFile> multipartFileList) {

        Long loginUserId = SecurityContextHolderUtil.getMemberId();

        if(feedId == null) {
            throw new IllegalArgumentException("FeedId cannot be null");
        }

        if(!feedId.equals(loginUserId)) {
            throw new BadRequestException("BadRequest, only can update own feeds");
        }

        Feed feed = feedRepository.findById(feedId).orElseThrow(DataNotFoundException::new);

        // TODO :: 최적화 필요, 매 요청마다 S3 접근 -> 삭제 -> 업로드 진행
        s3Service.deleteS3Object(feed.getFileUrl());
        String fileUrl = s3Service.uploadFile(feed.getId().toString(), feed.getFeedType(), multipartFileList);

        feed.changeTitle(updateFeedRequestDto.title());
        feed.changeContents(updateFeedRequestDto.contents());
        feed.setFileUrl(fileUrl);
    }
}
