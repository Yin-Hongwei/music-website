package com.yin.music.config;

import com.yin.music.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mitigate cookie CSRF: mutating requests that carry a session must come from an allowed Origin/Referer.
 * Complements SameSite=Lax session cookies (see application-*.properties).
 */
@Component
public class CsrfOriginInterceptor implements HandlerInterceptor {

    private Set<String> allowedOrigins = java.util.Collections.emptySet();

    @Value("${app.security.cors.allowed-origins:http://localhost:3000}")
    public void setAllowedOriginsCsv(String csv) {
        this.allowedOrigins = Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)
                || "HEAD".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            return true;
        }

        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isBlank()) {
            if (isAllowedOrigin(origin.trim())) {
                return true;
            }
            throw new UnauthorizedException("非法请求来源");
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            if (isAllowedReferer(referer.trim())) {
                return true;
            }
            throw new UnauthorizedException("非法请求来源");
        }

        // Browser CSRF usually sends Origin or Referer; reject session-bound mutations without either.
        throw new UnauthorizedException("非法请求来源");
    }

    private boolean isAllowedOrigin(String origin) {
        String normalized = origin.toLowerCase(Locale.ROOT);
        if (allowedOrigins.contains(normalized)) {
            return true;
        }
        // Support patterns like http://localhost:* already expanded in config as concrete origins.
        return false;
    }

    private boolean isAllowedReferer(String referer) {
        try {
            URI uri = URI.create(referer);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (scheme == null || host == null) {
                return false;
            }
            int port = uri.getPort();
            String origin = port > 0
                    ? scheme.toLowerCase(Locale.ROOT) + "://" + host.toLowerCase(Locale.ROOT) + ":" + port
                    : scheme.toLowerCase(Locale.ROOT) + "://" + host.toLowerCase(Locale.ROOT);
            return isAllowedOrigin(origin);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
