package com.yin.music.support;

import com.yin.music.exception.UnauthorizedException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * App-user / admin identity bound to {@link HttpSession} after login.
 * Mutating client APIs must use this identity instead of trusting client-supplied userId/id alone.
 * Admin session ({@link #ATTR_ADMIN_NAME}) may act on a requested target user where the manage UI needs it.
 */
public final class SessionUser {

    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_USERNAME = "username";
    /** Set by admin login; kept as {@code name} for backward compatibility. */
    public static final String ATTR_ADMIN_NAME = "name";

    private SessionUser() {
    }

    /** Invalidate any pre-login session to mitigate session fixation. */
    public static HttpSession rotate(HttpServletRequest request) {
        HttpSession old = request.getSession(false);
        if (old != null) {
            try {
                old.invalidate();
            } catch (IllegalStateException ignored) {
                // already invalidated
            }
        }
        return request.getSession(true);
    }

    public static void bind(HttpSession session, Integer userId, String username) {
        session.setAttribute(ATTR_USER_ID, userId);
        session.setAttribute(ATTR_USERNAME, username);
    }

    public static void bindAdmin(HttpSession session, String adminName) {
        session.setAttribute(ATTR_ADMIN_NAME, adminName);
    }

    public static Integer getUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(ATTR_USER_ID);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    public static boolean isAdmin(HttpSession session) {
        if (session == null) {
            return false;
        }
        Object name = session.getAttribute(ATTR_ADMIN_NAME);
        return name instanceof String && StringUtils.isNotBlank((String) name);
    }

    public static Integer requireUserId(HttpSession session) {
        Integer userId = getUserId(session);
        if (userId == null) {
            throw new UnauthorizedException("请先登录");
        }
        return userId;
    }

    public static void requireAdmin(HttpSession session) {
        if (!isAdmin(session)) {
            throw new UnauthorizedException("请先登录管理后台");
        }
    }

    /**
     * App user may only act as themselves; admin may act on {@code requestedUserId}.
     */
    public static Integer resolveMutableUserId(HttpSession session, Integer requestedUserId) {
        if (isAdmin(session)) {
            if (requestedUserId == null) {
                throw new UnauthorizedException("缺少用户 ID");
            }
            return requestedUserId;
        }
        return requireUserId(session);
    }

    public static void requireAppUserOrAdmin(HttpSession session) {
        if (getUserId(session) == null && !isAdmin(session)) {
            throw new UnauthorizedException("请先登录");
        }
    }
}
