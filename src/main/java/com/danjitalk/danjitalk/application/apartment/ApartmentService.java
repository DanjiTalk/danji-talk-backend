package com.danjitalk.danjitalk.application.apartment;

import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentInfoResponse;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentRegisterRequest;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentRegisterResponse;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.s3.dto.response.S3FileUrlResponseDto;
import com.danjitalk.danjitalk.domain.s3.enums.FileType;
import com.danjitalk.danjitalk.event.dto.RecentComplexViewedEvent;
import com.danjitalk.danjitalk.event.dto.GroupChatCreateEvent;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.s3.S3Service;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final S3Service s3Service;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 아파트 단지 등록
     * @param request
     * @param multipartFileList
     * @return
     */
    @Transactional
    public ApartmentRegisterResponse registerApartment(ApartmentRegisterRequest request, List<MultipartFile> multipartFileList) {
        // 파일이 10개 이상이면 Exception
        if(multipartFileList != null && multipartFileList.size() > 10) {
            throw new IllegalArgumentException("More than 10 Files");
        }

        S3FileUrlResponseDto s3FileUrlResponseDto = null;

        // 파일이 있으면 S3에 업로드
        if(multipartFileList != null && !multipartFileList.isEmpty()) {
            s3FileUrlResponseDto = s3Service.uploadFiles(FileType.APARTMENT, multipartFileList);
        }

        // 동 범위, 동 수 분리
        String[] parts = request.buildingRange().split(" \\(");
        String buildingRange = parts[0];

        String buildingCountText = parts[1].replaceAll("[^0-9]", ""); // 숫자만 남김
        int buildingCount = Integer.parseInt(buildingCountText);

        Apartment apartment = Apartment.builder()
                .name(request.name())
                .region(request.region())
                .location(request.location())
                .totalUnit(request.totalUnit())
                .parkingCapacity(request.parkingCapacity())
                .buildingCount(buildingCount)
                .buildingRange(buildingRange)
                .fileUrl(s3FileUrlResponseDto != null ? s3FileUrlResponseDto.fileUrl() : null)
                .thumbnailFileUrl(s3FileUrlResponseDto != null ? s3FileUrlResponseDto.thumbnailFileUrl() : null)
                .build();

        apartmentRepository.save(apartment);

        applicationEventPublisher.publishEvent(new GroupChatCreateEvent(request.name()));

        return ApartmentRegisterResponse.builder()
                .id(apartment.getId())
                .name(apartment.getName())
                .region(apartment.getRegion())
                .location(apartment.getLocation())
                .totalUnit(apartment.getTotalUnit())
                .parkingCapacity(apartment.getParkingCapacity())
                .buildingCount(apartment.getBuildingCount())
                .fileUrl(apartment.getFileUrl())
                .build();
    }

    @Transactional(readOnly = true)
    public ApartmentInfoResponse getApartmentInfo(Long id) {
        String key = "apartment:detail:" + id;
        Object object = redisTemplate.opsForValue().get(key);

        if(object instanceof ApartmentInfoResponse response) {
            log.info("response {}", response);
            return response;
        }

        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 아파트입니다."));
        Long currentMemberId = SecurityContextHolderUtil.getMemberIdOptional().orElse(0L);

        // 최근 본 단지 저장 이벤트
        if (currentMemberId != 0) {
            applicationEventPublisher.publishEvent(
                new RecentComplexViewedEvent(
                    apartment.getId(),
                    apartment.getName(),
                    apartment.getRegion(),
                    apartment.getLocation(),
                    apartment.getTotalUnit(),
                    apartment.getBuildingCount(),
                    apartment.getThumbnailFileUrl(),
                    currentMemberId
                )
            );
        }

        ApartmentInfoResponse apartmentInfoResponse = ApartmentInfoResponse.builder()
                .name(apartment.getName())
                .region(apartment.getRegion())
                .location(apartment.getLocation())
                .totalUnit(apartment.getTotalUnit())
                .parkingCapacity(apartment.getParkingCapacity())
                .buildingCount(apartment.getBuildingCount())
                .buildingRange(apartment.getBuildingRange())
                .fileUrl(apartment.getFileUrl())
                .chatroomId(apartment.getChatroomId())
                .build();

        redisTemplate.opsForValue().set(key, apartmentInfoResponse, Duration.ofHours(1));

        return apartmentInfoResponse;
    }
}
