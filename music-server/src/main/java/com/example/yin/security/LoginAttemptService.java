package com.example.yin.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static class AttemptRecord {
        private int failedCount;
        private long lockUntilMillis;
    }

    private final Map<String, AttemptRecord> attempts = new ConcurrentHashMap<>();

    @Value("${app.security.login.max-failed-attempts:5}")
    private int maxFailedAttempts;

    @Value("${app.security.login.lock-minutes:10}")
    private int lockMinutes;

    public boolean isBlocked(String account, HttpServletRequest request) {
        AttemptRecord record = attempts.get(buildKey(account, request));
        if (record == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (record.lockUntilMillis <= now) {
            attempts.remove(buildKey(account, request));
            return false;
        }
        return true;
    }

    public long getRemainingSeconds(String account, HttpServletRequest request) {
        AttemptRecord record = attempts.get(buildKey(account, request));
        if (record == null) {
            return 0;
        }
        long now = System.currentTimeMillis();
        if (record.lockUntilMillis <= now) {
            return 0;
        }
        return (record.lockUntilMillis - now) / 1000;
    }

    public void onLoginSuccess(String account, HttpServletRequest request) {
        attempts.remove(buildKey(account, request));
    }

    public void onLoginFailure(String account, HttpServletRequest request) {
        String key = buildKey(account, request);
        AttemptRecord record = attempts.computeIfAbsent(key, k -> new AttemptRecord());
        record.failedCount += 1;
        if (record.failedCount >= maxFailedAttempts) {
            record.lockUntilMillis = System.currentTimeMillis() + lockMinutes * 60L * 1000L;
        }
    }

    private String buildKey(String account, HttpServletRequest request) {
        return normalizeIp(getClientIp(request)) + ":" + normalizeAccount(account);
    }

    private String normalizeAccount(String account) {
        return account == null ? "" : account.trim().toLowerCase();
    }

    private String normalizeIp(String ip) {
        if (ip == null || ip.isBlank()) {
            return "unknown";
        }
        return ip.trim();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
