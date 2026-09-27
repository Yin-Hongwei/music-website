package com.yin.music.support;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Build filesystem-safe upload object names and enforce extension / MIME allowlists.
 * Blocks stored XSS via {@code .html}/{@code .svg}/script uploads on static ResourceHandlers.
 */
public final class SafeFilenames {

    public enum Kind {
        IMAGE,
        AUDIO
    }

    private static final Set<String> IMAGE_EXTS = unmodifiable(
            ".jpg", ".jpeg", ".png", ".gif", ".webp");

    private static final Set<String> AUDIO_EXTS = unmodifiable(
            ".mp3", ".flac", ".wav", ".aac", ".m4a", ".ogg");

    private static final Set<String> BLOCKED_CONTENT_TYPES = unmodifiable(
            "text/html",
            "application/xhtml+xml",
            "image/svg+xml",
            "text/javascript",
            "application/javascript",
            "application/x-javascript",
            "text/xml",
            "application/xml");

    private SafeFilenames() {
    }

    public static String uniqueImage(String originalName, String fallback) {
        return unique(originalName, fallback, Kind.IMAGE);
    }

    public static String uniqueAudio(String originalName, String fallback) {
        return unique(originalName, fallback, Kind.AUDIO);
    }

    /** @deprecated Prefer {@link #uniqueImage} / {@link #uniqueAudio}. */
    public static String unique(String originalName, String fallback) {
        return uniqueImage(originalName, fallback);
    }

    public static String unique(String originalName, String fallback, Kind kind) {
        requireAllowed(originalName, null, kind);
        String ext = asciiExtension(originalName, fallback, allowedExts(kind));
        return System.currentTimeMillis() + ext;
    }

    /**
     * Validate original filename extension and declared Content-Type before {@code transferTo}.
     */
    public static void requireAllowed(MultipartFile file, Kind kind) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("请上传文件");
        }
        requireAllowed(file.getOriginalFilename(), file.getContentType(), kind);
    }

    public static void requireAllowed(String originalName, String contentType, Kind kind) {
        Set<String> allowed = allowedExts(kind);
        String ext = extractAsciiExtension(originalName);
        if (ext == null || !allowed.contains(ext)) {
            throw new IllegalArgumentException(kind == Kind.AUDIO
                    ? "仅支持音频文件（mp3/flac/wav/aac/m4a/ogg）"
                    : "仅支持图片文件（jpg/png/gif/webp）");
        }
        if (contentType != null && !contentType.isBlank()) {
            String ct = contentType.toLowerCase(Locale.ROOT).trim();
            int semi = ct.indexOf(';');
            if (semi >= 0) {
                ct = ct.substring(0, semi).trim();
            }
            if (BLOCKED_CONTENT_TYPES.contains(ct)) {
                throw new IllegalArgumentException("不支持的文件类型");
            }
            if (kind == Kind.IMAGE && !(ct.startsWith("image/") || "application/octet-stream".equals(ct))) {
                throw new IllegalArgumentException("仅支持图片文件");
            }
            if (kind == Kind.AUDIO && !(ct.startsWith("audio/") || "application/octet-stream".equals(ct))) {
                throw new IllegalArgumentException("仅支持音频文件");
            }
        }
    }

    private static String asciiExtension(String originalName, String fallback, Set<String> allowed) {
        String ext = extractAsciiExtension(originalName);
        if (ext != null && allowed.contains(ext)) {
            return ext;
        }
        String fallbackExt = extractAsciiExtension(fallback);
        if (fallbackExt != null && allowed.contains(fallbackExt)) {
            return fallbackExt;
        }
        throw new IllegalArgumentException("不支持的文件类型");
    }

    private static String extractAsciiExtension(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return null;
        }
        String name = originalName;
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0) {
            name = name.substring(slash + 1);
        }
        if (name.isBlank() || name.contains("..")) {
            return null;
        }
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot >= name.length() - 1) {
            return null;
        }
        String ext = name.substring(dot).toLowerCase(Locale.ROOT);
        if (ext.matches("\\.[a-z0-9]{1,8}")) {
            return ext;
        }
        return null;
    }

    private static Set<String> allowedExts(Kind kind) {
        return kind == Kind.AUDIO ? AUDIO_EXTS : IMAGE_EXTS;
    }

    private static Set<String> unmodifiable(String... values) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }
}
