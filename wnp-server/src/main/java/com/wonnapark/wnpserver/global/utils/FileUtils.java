package com.wonnapark.wnpserver.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {
    private static final String MULTIPART_FILE_TRANSFER_ERROR = "MULTIPART 파일을 FILE로 변환 중 오류가 발생했습니다.";

    public static class FileIOException extends RuntimeException {

        public FileIOException(String message, Throwable e) {
            super(message, e);
        }

    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("temp", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new FileIOException(MULTIPART_FILE_TRANSFER_ERROR, e);
        }
    }

}
