package com.danjitalk.danjitalk.domain.s3.dto.response;

public record S3FileUrlResponseDto(
        String fileUrl,
        String thumbnailFileUrl
) {
}
