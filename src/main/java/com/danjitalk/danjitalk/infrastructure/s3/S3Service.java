package com.danjitalk.danjitalk.infrastructure.s3;

import com.danjitalk.danjitalk.common.util.FileSignatureValidator;
import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;
import com.danjitalk.danjitalk.domain.s3.dto.response.S3ObjectResponseDto;
import com.danjitalk.danjitalk.infrastructure.s3.properties.S3ConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(".jpeg", ".png", ".jpg");

    private final S3ConfigProperties s3ConfigProperties;
    private final S3Client s3Client;

    /**
     * S3 파일 업로드
     * @param id ID값
     * @param fileType feed, MemberImg 등 파일 형식
     * @param multipartFileList 멀티파트파일
     * @return String {fileType}/{id}
     * */
    public String uploadFile(String id, FeedType fileType, List<MultipartFile> multipartFileList) {

        String urlKey = String.format("%s/%s", fileType.toString().toLowerCase(), id);

        multipartFileList.forEach(
                (file) -> {

                    try {
                        byte[] bytes = file.getBytes();
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                        String fileExtension = this.getFileExtension(file);

                        if (fileExtension == null || !ALLOWED_IMAGE_TYPES.contains(fileExtension)) {
                            throw new IllegalArgumentException("Unsupported content type: " + fileExtension);
                        }

                        if (!FileSignatureValidator.isImageSignature(byteArrayInputStream)) {
                            throw new IllegalArgumentException("Not allowed image signature, Only JPG, JPEG and PNG");
                        }

                        String formattedKey = String.format("feed/%s/%s%s", id, UUID.randomUUID(), fileExtension);

                        try ( ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
                            s3Client.putObject(
                                    PutObjectRequest.builder()
                                            .key(formattedKey)
                                            .bucket(s3ConfigProperties.getBucketName())
                                            .contentType(file.getContentType())
                                            .build(),
                                    RequestBody.fromInputStream(inputStream, bytes.length)
                            );
                        }

                    } catch (IOException ioException) {
                        throw new RuntimeException("Failed to upload file: ", ioException);
                    }

                }
        );

        return urlKey;

    }

    /**
     * 조회
     * @param url String {fileType}/{id}
     * @return List<S3ObjectResponseDto>
     * */
    public List<S3ObjectResponseDto> getS3Object(String url) {
        ListObjectsV2Request s3Request = ListObjectsV2Request.builder()
                .bucket(s3ConfigProperties.getBucketName())
                .prefix(url)
                .build();

        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(s3Request);

        return listObjectsV2Response.contents().stream().map(data ->
                new S3ObjectResponseDto(String.format("https://s3.%s.amazonaws.com/%s/%s", s3ConfigProperties.getRegion(), s3ConfigProperties.getBucketName(), data.key()))
        ).toList();
    }

    /**
     * URL Object 삭제
     * @param url {fileType}/{id}
     * */
    public void deleteS3Object(String url) {
        ListObjectsV2Request s3Request = ListObjectsV2Request.builder()
                .bucket(s3ConfigProperties.getBucketName())
                .prefix(url)
                .build();

        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(s3Request);

        listObjectsV2Response.contents().forEach(data -> {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(s3ConfigProperties.getBucketName())
                            .key(data.key())
                            .build()
            );
        });
    }

    /**
     * 파일 확장자명 추출
     * @param file 파일
     * @return String 확장자 타입
     * */
    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(fileName == null || fileName.isEmpty()) {
            return null;
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }

}
