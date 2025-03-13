package com.danjitalk.danjitalk.application.community.feed;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.CreateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.GetFeedListRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.request.UpdateFeedRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.*;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.s3.dto.response.S3FileUrlResponseDto;
import com.danjitalk.danjitalk.domain.s3.dto.response.S3ObjectResponseDto;
import com.danjitalk.danjitalk.domain.s3.enums.FileType;
import com.danjitalk.danjitalk.domain.user.member.dto.response.FeedMemberResponseDto;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import com.danjitalk.danjitalk.infrastructure.s3.S3Service;
import com.danjitalk.danjitalk.infrastructure.s3.properties.S3ConfigProperties;
import io.jsonwebtoken.lang.Objects;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private final ApartmentRepository apartmentRepository;
    private final S3ConfigProperties s3ConfigProperties;

    /**
     * 피드 목록 조회
     * limit 15, cursorDate 필요
     * */
    public FeedListDto getFeedList(Long apartmentId, LocalDateTime cursorDate) {

        List<ProjectionFeedDto> projectionFeedList = feedRepository.getProjectionFeedList(apartmentId, cursorDate).orElseThrow(() -> new IllegalArgumentException("No feeds found"));

        ProjectionFeedDto lastIndexFeedDto = projectionFeedList.get(projectionFeedList.size() - 1);

        List<FeedDto> list = projectionFeedList.stream().map(projectionFeedDto -> {
                String thumbnailFileUrl = null;

                // 썸네일이 있으면 반환
                if(projectionFeedDto.thumbnailFileUrl() != null){
                    thumbnailFileUrl = "https://s3." + s3ConfigProperties.getRegion() + ".amazonaws.com/" + s3ConfigProperties.getBucketName() + "/" + projectionFeedDto.thumbnailFileUrl();
                }

                return new FeedDto(
                        projectionFeedDto.feedId(),
                        projectionFeedDto.memberId(),
                        projectionFeedDto.nickName(),
                        projectionFeedDto.title(),
                        projectionFeedDto.contents(),
                        projectionFeedDto.localDateTime(),
                        projectionFeedDto.reactionCount(),
                        projectionFeedDto.commentCount(),
                        thumbnailFileUrl
                );
            }
        ).toList();

        return new FeedListDto(list, lastIndexFeedDto.localDateTime(), projectionFeedList.size());
    }

    /**
     * 피드 상세 조회
     * @param feedId 피드 ID
     * @return FeedDetailResponseDto
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

    /**
     * 피드 작성
     * @param createFeedRequestDto DTO
     * @param multipartFileList 파일 List
     * @return 생성된 Feed 의 데이터
     * */
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

        S3FileUrlResponseDto s3FileUrlResponseDto = null;

        // 파일이 있으면 S3에 업로드
        if(multipartFileList != null && !multipartFileList.isEmpty()) {
            s3FileUrlResponseDto = s3Service.uploadFiles(FileType.fromFeedType(createFeedRequestDto.feedType()), multipartFileList);
        }

        Member member = memberRepository.findById(SecurityContextHolderUtil.getMemberId()).orElseThrow(() -> new DataNotFoundException());
        Apartment apartment = apartmentRepository.findById(createFeedRequestDto.apartmentId()).orElseThrow(() -> new DataNotFoundException());

        Feed feed = Feed.builder()
                .title(createFeedRequestDto.title())
                .contents(createFeedRequestDto.contents())
                .feedType(createFeedRequestDto.feedType())
                .fileUrl(s3FileUrlResponseDto != null ? s3FileUrlResponseDto.fileUrl() : null)
                .thumbnailFileUrl(s3FileUrlResponseDto != null ? s3FileUrlResponseDto.thumbnailFileUrl() : null)
                .member(member)
                .apartment(apartment)
                .build();

        feedRepository.save(feed);

        return new CreateFeedResponseDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContents(),
                feed.getFeedType(),
                feed.getFileUrl()
        );
    }

    /**
     * 피드 업데이트
     * @param feedId - 피드 ID
     * @param updateFeedRequestDto - 업데이트할 내용 DTO
     * @param multipartFileList - 업데이트 할 파일
     * */
    @Transactional
    public void updateFeed(Long feedId, UpdateFeedRequestDto updateFeedRequestDto, List<MultipartFile> multipartFileList, List<String> deleteFileUrls) {

        Long loginUserId = SecurityContextHolderUtil.getMemberId();

        if(feedId == null) {
            throw new IllegalArgumentException("FeedId cannot be null");
        }


        Feed feed = feedRepository.findById(feedId).orElseThrow(DataNotFoundException::new);

        if(!loginUserId.equals(feed.getMember().getId())) {
            throw new BadRequestException("BadRequest, only can update own feeds");
        }

        // TODO :: 최적화 필요, 매 요청마다 S3 접근 -> 삭제 -> 업로드 진행
        if(!Objects.isEmpty(deleteFileUrls)) {
            s3Service.deleteS3MultipleObjects(deleteFileUrls);

            // s3 스토리지에 이미지가 남아있는지 확인
            if(Objects.isEmpty(s3Service.getS3Object(feed.getFileUrl()))) {
                feed.setFileUrl(null);
                feed.setThumbnailFileUrl(null);
            }
        }

        if(!Objects.isEmpty(multipartFileList)) {
            // S3 스토리지에 남은 데이터가 없으면 새로 업데이트 + feed entity 에 설정, 있으면 파일만 추가
            if(feed.getFileUrl() == null) {
                S3FileUrlResponseDto s3FileUrlResponseDto = s3Service.uploadFiles(FileType.fromFeedType(feed.getFeedType()), multipartFileList);

                if(s3FileUrlResponseDto != null) {
                    feed.setFileUrl(s3FileUrlResponseDto.fileUrl());
                    feed.setThumbnailFileUrl(s3FileUrlResponseDto.thumbnailFileUrl());
                }
            } else {
                s3Service.addUploadFiles(feed.getFileUrl(), multipartFileList);
            }
        }

        feed.changeTitle(updateFeedRequestDto.title());
        feed.changeContents(updateFeedRequestDto.contents());
    }

    /**
     * 피드 삭제
     * 피드 entity, S3 파일 등을 삭제한다.
     * 댓글, 좋아요 등도 같이 삭제할지는 고민
     *
     * @param feedId 피드 Id
     * */
    @Transactional
    public void deleteFeed(Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(DataNotFoundException::new);

        Long loginUserId = SecurityContextHolderUtil.getMemberId();

        if(!loginUserId.equals(feed.getMember().getId())) {
            throw new BadRequestException("BadRequest, only can delete own feeds");
        }

        if(feed.getFileUrl() != null) {
            s3Service.deleteS3Object(feed.getFileUrl());
        }

        feedRepository.delete(feed);
    }
}
