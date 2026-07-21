package com.yin.music.support;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * MinIO object read helper (infrastructure). Controllers use this for getObject
 * with local fallback via {@link LocalMediaStore}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioObjectStore {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * @return object bytes, or {@code null} if missing / MinIO unavailable
     */
    public byte[] getObject(String objectKey) {
        if (objectKey == null || objectKey.isEmpty() || objectKey.contains("..")) {
            return null;
        }
        try (InputStream in = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectKey).build())) {
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.debug("MinIO getObject skipped for {}: {}", objectKey, e.getMessage());
            return null;
        }
    }
}
