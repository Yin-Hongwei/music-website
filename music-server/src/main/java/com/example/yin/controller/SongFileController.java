package com.example.yin.controller;

import com.example.yin.constant.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

@RestController
public class SongFileController {

    @GetMapping("/song-file")
    public ResponseEntity<Resource> streamSongFileByPath(@RequestParam("path") String path) {
        Path songDir = Paths.get(Constants.PROJECT_PATH, "song");
        String fileName = path;
        if (fileName.startsWith("/song/")) {
            fileName = fileName.substring("/song/".length());
        }
        Path target = resolveExistingFile(songDir, fileName);
        if (target == null) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(target.toFile());
        MediaType contentType = MediaTypeFactory.getMediaType(target.getFileName().toString())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        try {
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(Files.size(target))
                    .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/song/{fileName:.+}")
    public ResponseEntity<Resource> streamSongFile(@PathVariable String fileName) {
        Path songDir = Paths.get(Constants.PROJECT_PATH, "song");
        Path target = resolveExistingFile(songDir, fileName);
        if (target == null) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(target.toFile());
        MediaType contentType = MediaTypeFactory.getMediaType(target.getFileName().toString())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        try {
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(Files.size(target))
                    .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private Path resolveExistingFile(Path songDir, String fileName) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(fileName);
        candidates.add(decodeUtf8(fileName));
        candidates.add(recodeIso88591ToUtf8(fileName));
        candidates.add(decodeUtf8(recodeIso88591ToUtf8(fileName)));

        for (String candidate : candidates) {
            if (candidate == null || candidate.isBlank()) {
                continue;
            }
            Path path = songDir.resolve(candidate).normalize();
            if (!path.startsWith(songDir)) {
                continue;
            }
            if (Files.exists(path) && Files.isRegularFile(path)) {
                return path;
            }
        }
        return null;
    }

    private String decodeUtf8(String value) {
        try {
            return java.net.URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            return value;
        }
    }

    private String recodeIso88591ToUtf8(String value) {
        return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
