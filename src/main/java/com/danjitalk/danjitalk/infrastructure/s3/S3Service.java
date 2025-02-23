package com.danjitalk.danjitalk.infrastructure.s3;

import com.danjitalk.danjitalk.common.util.FileSignatureValidator;
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

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png");

    private final S3ConfigProperties s3ConfigProperties;
    private final S3Client s3Client;

    /**
     * S3 파일 업로드
     * @param multipartFileList 멀티파트파일
     * @param id ID값
     * @param fileType feed, MemberImg 등 파일 형식
     * @return String {fileType}/{id}
     * */
    public String uploadFile(List<MultipartFile> multipartFileList, Long id, String fileType) {

        String urlKey = String.format("%s/%d", fileType, id);

        multipartFileList.forEach(
                (file) -> {

                    try {
                        byte[] bytes = file.getBytes();
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);


                        String contentType = file.getContentType();
                        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
                            throw new IllegalArgumentException("Unsupported content type: " + contentType);
                        }

                        if (FileSignatureValidator.isImageSignature(byteArrayInputStream)) {
                            throw new IllegalArgumentException("Not allowed image signature, Only JPG, JPEG and PNG");
                        }

                        String extensionByMimeType = this.getExtensionByMimeType(contentType);

                        String formattedKey = String.format("feed/%d/%s%s", id, UUID.randomUUID(), extensionByMimeType);

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
     * */
    public void getS3Object(String url) {
        ListObjectsV2Request s3Request = ListObjectsV2Request.builder()
                .bucket(s3ConfigProperties.getBucketName())
                .prefix(url)
                .build();

        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(s3Request);

        List<String> urlList = listObjectsV2Response.contents().stream().map(data ->
                String.format("https://s3.%s.amazonaws.com/%s/%s", s3ConfigProperties.getRegion(), s3ConfigProperties.getBucketName(), data.key())
        ).toList();

        // TODO::return DTO
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
     * MIME 타입 기반 확장자 추출
     * @param mimeType MIME 타입
     * @return String 확장자 타입
     * */
    private String getExtensionByMimeType(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new IllegalArgumentException("Unsupported mime type: " + mimeType);
        };
    }

}
