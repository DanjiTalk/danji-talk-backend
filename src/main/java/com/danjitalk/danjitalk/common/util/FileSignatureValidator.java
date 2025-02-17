package com.danjitalk.danjitalk.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 파일 시그니처 확인
 * */
public class FileSignatureValidator {

    private static final byte[] JPEG_SIGNATURE = {(byte) 0xFF, (byte) 0xD8};
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A};

    /**
     * 파일 시그니처 확인
     * @param inputStream 파일 InputStream
     * @return boolean
     * @throws IOException 파일 읽기 예외 처리
     * */
    public static boolean isImageSignature(InputStream inputStream) throws IOException {
        byte[] fileHeader = new byte[4];
        int bytesRead = inputStream.read(fileHeader);

        if(bytesRead < 4) {
            return false;
        }

        return startsWith(fileHeader, JPEG_SIGNATURE) || startsWith(fileHeader, PNG_SIGNATURE);
    }

    /**
     * 파일 헤더의 시그니처 부분 확인
     * @param fileHeader 읽은 파일 헤더 (4byte)
     * @param signature 파일 시그니처
     * @return boolean
     * */
    public static boolean startsWith(byte[] fileHeader, byte[] signature) {

        // 시그니처가 4바이트 이상일떄(PNG) 시그니처 앞 4byte 만 사용
        if (fileHeader.length < signature.length) {
            signature = Arrays.copyOf(signature, fileHeader.length);
        }

        // 시그니처와 다르면 false
        for (int i = 0; i < signature.length; i++) {
            if (fileHeader[i] != signature[i]) {
                return false;
            }
        }

        return true;
    }

}
