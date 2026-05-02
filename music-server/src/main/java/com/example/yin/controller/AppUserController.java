package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.AppUserRequest;
import com.example.yin.model.request.PasswordResetRequest;
import com.example.yin.security.LoginAttemptService;
import com.example.yin.service.AppUserService;
import com.example.yin.service.PasswordResetService;
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
    public R addUser(@RequestBody AppUserRequest registryRequest) {
        return appUserService.addUser(registryRequest);
    }

    @GetMapping("/user/sendVerificationCode")
    public R sendVerificationCode(@RequestParam String email) {
        return passwordResetService.sendVerificationCode(email);
    }

    @PostMapping("/user/resetPassword")
    public R resetPassword(@RequestBody PasswordResetRequest request) {
        return passwordResetService.resetPassword(request);
    }

    @PostMapping("/user/login/status")
    public R loginStatus(@RequestBody AppUserRequest loginRequest, HttpSession session, HttpServletRequest request) {
        String account = loginRequest.getUsername();
        if (loginAttemptService.isBlocked(account, request)) {
            long remain = loginAttemptService.getRemainingSeconds(account, request);
            return R.error("登录失败次数过多，请" + remain + "秒后重试");
        }

        R result = appUserService.loginStatus(loginRequest, session);
        if (Boolean.TRUE.equals(result.getSuccess())) {
            loginAttemptService.onLoginSuccess(account, request);
        } else {
            loginAttemptService.onLoginFailure(account, request);
        }
        return result;
    }

    @GetMapping("/user")
    public R allUser() {
        return appUserService.allUser();
    }

    @GetMapping("/user/page")
    public R pageUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return appUserService.pageUser(safePage, safeSize);
    }

    @GetMapping("/user/detail")
    public R userOfId(@RequestParam int id) {
        return appUserService.userOfId(id);
    }

    @GetMapping("/user/delete")
    public R deleteUser(@RequestParam int id) {
        return appUserService.deleteUser(id);
    }

    @PostMapping("/user/update")
    public R updateUserMsg(@RequestBody AppUserRequest updateRequest) {
        return appUserService.updateUserMsg(updateRequest);
    }

    @PostMapping("/user/updatePassword")
    public R updatePassword(@RequestBody AppUserRequest updatePasswordRequest) {
        return appUserService.updatePassword(updatePasswordRequest);
    }

    @PostMapping("/user/avatar/update")
    public R updateUserPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id") int id) {
        return appUserService.updateUserAvatar(avatarFile, id);
    }
}
