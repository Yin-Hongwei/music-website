package com.yin.music.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.yin.music.config.MediaProperties;

import lombok.RequiredArgsConstructor;

/**
 * 本地 {@code app.media.root} 下的安全读路径（防路径穿越）。
 */
@Component
@RequiredArgsConstructor
public class LocalMediaStore {

    private final MediaProperties mediaProperties;

    public byte[] readSong(String fileName) {
        return readUnderDirectory(mediaProperties.songDir(), fileName);
    }

    public byte[] readImg(String subDir, String fileName) {
        return readUnderDirectory(mediaProperties.imgDir(subDir), fileName);
    }

    public Path resolveSong(String fileName) {
        Path target = resolveUnderDirectory(mediaProperties.songDir(), fileName);
        if (target == null || !Files.isRegularFile(target)) {
            return null;
        }
        return target;
    }

    public byte[] readUnderDirectory(Path directory, String fileName) {
        Path target = resolveUnderDirectory(directory, fileName);
        if (target == null || !Files.isRegularFile(target)) {
            return null;
        }
        try {
            return Files.readAllBytes(target);
        } catch (IOException e) {
            return null;
        }
    }

    public Path resolveUnderDirectory(Path directory, String fileName) {
        if (directory == null || fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            return null;
        }
        Path base = directory.toAbsolutePath().normalize();
        Path target = base.resolve(fileName).normalize();
        if (!target.startsWith(base)) {
            return null;
        }
        return target;
    }
}
