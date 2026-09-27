package com.yin.music.controller;

import com.yin.music.model.R;
import com.yin.music.model.request.AppUserRequest;
import com.yin.music.model.request.PasswordResetRequest;
import com.yin.music.support.LoginAttemptService;
import com.yin.music.support.SessionUser;
import com.yin.music.service.AppUserService;
import com.yin.music.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class AppUserController {

    private final AppUserService appUserService;

    private final LoginAttemptService loginAttemptService;

    private final PasswordResetService passwordResetService;

    @PostMapping("/user/add")
    public R<?> addUser(@RequestBody AppUserRequest registryRequest) {
        return appUserService.addUser(registryRequest);
    }

    @GetMapping("/user/sendVerificationCode")
    public R<?> sendVerificationCode(@RequestParam String email) {
        return passwordResetService.sendVerificationCode(email);
    }

    @PostMapping("/user/resetPassword")
    public R<?> resetPassword(@RequestBody PasswordResetRequest request) {
        return passwordResetService.resetPassword(request);
    }

    @PostMapping("/user/login/status")
    public R<?> loginStatus(@RequestBody AppUserRequest loginRequest, HttpServletRequest request) {
        String account = loginRequest.getUsername();
        if (loginAttemptService.isBlocked(account, request)) {
            long remain = loginAttemptService.getRemainingSeconds(account, request);
            return R.error("登录失败次数过多，请" + remain + "秒后重试");
        }

        R<?> result = appUserService.loginStatus(loginRequest, request);
        if (Boolean.TRUE.equals(result.getSuccess())) {
            loginAttemptService.onLoginSuccess(account, request);
        } else {
            loginAttemptService.onLoginFailure(account, request);
        }
        return result;
    }

    @GetMapping("/user")
    public R<?> allUser() {
        // Admin-only via AdminAuthInterceptor
        return appUserService.allUser();
    }

    @GetMapping("/user/page")
    public R<?> pageUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        // Admin-only via AdminAuthInterceptor
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return appUserService.pageUser(safePage, safeSize);
    }

    @GetMapping("/user/detail")
    public R<?> userOfId(@RequestParam int id, HttpSession session) {
        Integer viewerId = SessionUser.getUserId(session);
        boolean asAdmin = SessionUser.isAdmin(session);
        return appUserService.userOfId(id, viewerId, asAdmin);
    }

    @DeleteMapping("/user/delete")
    public R<?> deleteUser(@RequestParam int id, HttpSession session) {
        // App user may only delete self; admin may delete any target id.
        Integer targetUserId = SessionUser.resolveMutableUserId(session, id);
        R<?> result = appUserService.deleteUser(targetUserId);
        if (Boolean.TRUE.equals(result.getSuccess()) && !SessionUser.isAdmin(session)) {
            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
                // already invalidated
            }
        }
        return result;
    }

    @PostMapping("/user/update")
    public R<?> updateUserMsg(@RequestBody AppUserRequest updateRequest, HttpSession session) {
        Integer targetUserId = SessionUser.resolveMutableUserId(session, updateRequest.getId());
        return appUserService.updateUserMsg(updateRequest, targetUserId);
    }

    @PostMapping("/user/updatePassword")
    public R<?> updatePassword(@RequestBody AppUserRequest updatePasswordRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return appUserService.updatePassword(updatePasswordRequest, currentUserId);
    }

    @PostMapping("/user/avatar/update")
    public R<?> updateUserPic(
            @RequestParam("file") MultipartFile avatarFile,
            @RequestParam(value = "id", required = false) Integer id,
            HttpSession session
    ) {
        Integer targetUserId = SessionUser.resolveMutableUserId(session, id);
        return appUserService.updateUserAvatar(avatarFile, targetUserId);
    }
}
