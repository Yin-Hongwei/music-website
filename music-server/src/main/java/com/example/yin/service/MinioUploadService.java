package com.example.yin.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Uploads objects to MinIO (migrated from legacy MinioUploadController static helpers).
 */
@Service
@RequiredArgsConstructor
public class MinioUploadService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public boolean uploadSongFile(MultipartFile file) {
        String objectName = file.getOriginalFilename();
        return putObject(objectName, file);
    }

    public boolean uploadSingerImg(MultipartFile file, String objectFileName) {
        return putObject("singer/img/" + objectFileName, file);
    }

    public boolean uploadSongListImg(MultipartFile file, String objectFileName) {
        return putObject("songlist/" + objectFileName, file);
    }

    public boolean uploadSongCoverImg(MultipartFile file, String objectFileName) {
        return putObject("singer/song/" + objectFileName, file);
    }

    public boolean uploadAvatarImg(MultipartFile file, String objectFileName) {
        return putObject("img/avatorImages/" + objectFileName, file);
    }

    private boolean putObject(String objectName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            long size = file.getSize() >= 0 ? file.getSize() : inputStream.available();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return true;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }
}
