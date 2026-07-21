package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yin.music.support.LocalMediaStore;
import com.yin.music.support.MinioObjectStore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Unified media read endpoints: MinIO object read, local {@code data/} fallback,
 * avatar classpath fallback, song streaming (Range), and attachment download.
 */
@RestController
@RequiredArgsConstructor
public class MediaController {

    private static final String AVATAR_DIR = "avatarImages";

    private final MinioObjectStore minioObjectStore;
    private final LocalMediaStore localMediaStore;

    @GetMapping("/${minio.bucket-name}/singer/img/{fileName:.+}")
    public ResponseEntity<byte[]> getSingerImage(@PathVariable String fileName) {
        byte[] bytes = minioObjectStore.getObject("singer/img/" + fileName);
        if (bytes == null) {
            bytes = localMediaStore.readImg("singerPic", fileName);
        }
        return imageResponse(bytes);
    }

    @GetMapping("/${minio.bucket-name}/songlist/{fileName:.+}")
    public ResponseEntity<byte[]> getSongListImage(@PathVariable String fileName) {
        byte[] bytes = minioObjectStore.getObject("songlist/" + fileName);
        if (bytes == null) {
            bytes = localMediaStore.readImg("songListPic", fileName);
        }
        return imageResponse(bytes);
    }

    @GetMapping("/${minio.bucket-name}/singer/song/{fileName:.+}")
    public ResponseEntity<byte[]> getSongCoverImage(@PathVariable String fileName) {
        byte[] bytes = minioObjectStore.getObject("singer/song/" + fileName);
        if (bytes == null) {
            bytes = localMediaStore.readImg("songPic", fileName);
        }
        return imageResponse(bytes);
    }

    /** Audio files stored at bucket root (e.g. /user01/track.mp3). */
    @GetMapping("/${minio.bucket-name}/{fileName:.+}")
    public ResponseEntity<byte[]> getMusic(@PathVariable String fileName) {
        byte[] bytes = minioObjectStore.getObject(fileName);
        if (bytes == null) {
            bytes = localMediaStore.readSong(fileName);
        }
        if (bytes == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("/img/avatarImages/{fileName:.+}")
    public ResponseEntity<byte[]> getAvatarImage(@PathVariable String fileName) {
        return imageResponse(readAvatarBytes(fileName));
    }

    /**
     * Local song covers under {@code data/img/songPic}. Takes precedence over
     * ResourceHandler so non-ASCII filenames (URL-decoded / ISO-8859-1 mis-decoded)
     * still resolve — same strategy as {@link #streamLocalSong}.
     */
    @GetMapping("/img/songPic/{fileName:.+}")
    public ResponseEntity<byte[]> getLocalSongPic(@PathVariable String fileName) {
        byte[] bytes = readLocalImgWithEncoding("songPic", fileName);
        return imageResponse(bytes);
    }

    /**
     * Local song-list covers under {@code data/img/songListPic}. Needed so names with
     * {@code =} (common in seed/base64-like files) still resolve when the client
     * percent-encodes them as {@code %3D}.
     */
    @GetMapping("/img/songListPic/{fileName:.+}")
    public ResponseEntity<byte[]> getLocalSongListPic(@PathVariable String fileName) {
        byte[] bytes = readLocalImgWithEncoding("songListPic", fileName);
        return imageResponse(bytes);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        byte[] musicBytes = minioObjectStore.getObject(fileName);
        if (musicBytes == null) {
            musicBytes = localMediaStore.readSong(fileName);
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

    @GetMapping("/song-file")
    public ResponseEntity<Resource> streamSongFileByPath(@RequestParam("path") String path) {
        String fileName = path;
        if (fileName.startsWith("/song/")) {
            fileName = fileName.substring("/song/".length());
        }
        return streamLocalSong(fileName);
    }

    @GetMapping("/song/{fileName:.+}")
    public ResponseEntity<Resource> streamSongFile(@PathVariable String fileName) {
        return streamLocalSong(fileName);
    }

    private ResponseEntity<Resource> streamLocalSong(String fileName) {
        Path target = resolveExistingSong(fileName);
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

    private Path resolveExistingSong(String fileName) {
        for (String candidate : fileNameCandidates(fileName)) {
            Path path = localMediaStore.resolveSong(candidate);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    private byte[] readLocalImgWithEncoding(String subDir, String fileName) {
        for (String candidate : fileNameCandidates(fileName)) {
            byte[] bytes = localMediaStore.readImg(subDir, candidate);
            if (bytes != null) {
                return bytes;
            }
        }
        return null;
    }

    private static Set<String> fileNameCandidates(String fileName) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(fileName);
        candidates.add(decodeUtf8(fileName));
        candidates.add(recodeIso88591ToUtf8(fileName));
        candidates.add(decodeUtf8(recodeIso88591ToUtf8(fileName)));
        return candidates;
    }

    private byte[] readAvatarBytes(String fileName) {
        byte[] bytes = minioObjectStore.getObject("img/" + AVATAR_DIR + "/" + fileName);

        if (bytes == null) {
            bytes = localMediaStore.readImg(AVATAR_DIR, fileName);
        }
        if (bytes == null) {
            bytes = readClasspathStatic("static/img/" + AVATAR_DIR + "/" + fileName);
        }
        return bytes;
    }

    private ResponseEntity<byte[]> imageResponse(byte[] bytes) {
        if (bytes == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /** Default avatars bundled under {@code src/main/resources/static/...}. */
    private static byte[] readClasspathStatic(String classpathLocation) {
        if (classpathLocation == null || classpathLocation.contains("..")) {
            return null;
        }
        ClassPathResource res = new ClassPathResource(classpathLocation);
        if (!res.exists()) {
            return null;
        }
        try (InputStream in = res.getInputStream()) {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            return null;
        }
    }

    private static String decodeUtf8(String value) {
        try {
            return java.net.URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            return value;
        }
    }

    private static String recodeIso88591ToUtf8(String value) {
        return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
