package com.yin.music.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Safe local-file reads under a directory (path traversal guarded).
 */
public final class LocalFileSupport {

    private LocalFileSupport() {
    }

    public static byte[] readUnderDirectory(Path directory, String fileName) {
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

    public static Path resolveUnderDirectory(Path directory, String fileName) {
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
