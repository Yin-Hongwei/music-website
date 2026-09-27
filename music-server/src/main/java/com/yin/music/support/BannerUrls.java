package com.yin.music.support;

/**
 * Validate banner jump targets to prevent open redirects (e.g. {@code //evil.com}).
 */
public final class BannerUrls {

    private BannerUrls() {
    }

    /**
     * Empty string is allowed (no link). Otherwise only same-site absolute paths
     * ({@code /foo}) or http(s) URLs. Protocol-relative {@code //} and backslash tricks rejected.
     */
    public static String normalizeOrThrow(String raw) {
        if (raw == null) {
            return "";
        }
        String url = raw.trim();
        if (url.isEmpty()) {
            return "";
        }
        if (url.contains("\\") || url.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("非法链接");
        }
        if (url.startsWith("//")) {
            throw new IllegalArgumentException("非法链接");
        }
        if (url.startsWith("/")) {
            if (url.startsWith("//") || url.contains("://")) {
                throw new IllegalArgumentException("非法链接");
            }
            return url;
        }
        String lower = url.toLowerCase();
        if (lower.startsWith("https://") || lower.startsWith("http://")) {
            return url;
        }
        throw new IllegalArgumentException("链接仅支持站内路径或 http(s) 地址");
    }

    /** Client-side equivalent: true if the URL is safe to navigate. */
    public static boolean isSafe(String raw) {
        try {
            normalizeOrThrow(raw);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
