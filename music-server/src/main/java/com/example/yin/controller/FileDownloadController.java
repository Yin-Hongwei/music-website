package com.example.yin.controller;

import com.example.yin.constant.Constants;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/download")
@RequiredArgsConstructor
public class FileDownloadController {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        byte[] musicBytes = readFromMinio(fileName);
        if (musicBytes == null) {
            musicBytes = readLocalSong(fileName);
        }
        if (musicBytes == null) {
            return ResponseEntity.notFound().build();
        }
        ByteArrayResource resource = new ByteArrayResource(musicBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(musicBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private byte[] readFromMinio(String objectName) {
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).build())) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.debug("MinIO download skipped for {}: {}", objectName, e.getMessage());
            return null;
        }
    }

    private static byte[] readLocalSong(String fileName) {
        if (fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            return null;
        }
        Path base = Paths.get(Constants.PROJECT_PATH, "song").toAbsolutePath().normalize();
        Path target = base.resolve(fileName).normalize();
        if (!target.startsWith(base) || !Files.isRegularFile(target)) {
            return null;
        }
        try {
            return Files.readAllBytes(target);
        } catch (IOException e) {
            return null;
        }
    }
}
