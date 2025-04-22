package com.danjitalk.danjitalk.application.apartment;

import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentInfoResponse;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentRegisterRequest;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentRegisterResponse;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.s3.dto.response.S3FileUrlResponseDto;
import com.danjitalk.danjitalk.domain.s3.enums.FileType;
import com.danjitalk.danjitalk.event.dto.GroupChatCreateEvent;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

    public ApartmentInfoResponse getApartmentInfo(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 아파트입니다."));

        return ApartmentInfoResponse.builder()
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
    }
}
