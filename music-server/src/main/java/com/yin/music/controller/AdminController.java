package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yin.music.model.R;
import com.yin.music.model.request.AdminRequest;
import com.yin.music.service.AdminService;
import com.yin.music.support.LoginAttemptService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 后台管理的相关事宜
 */
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    private final LoginAttemptService loginAttemptService;

    // 判断是否登录成功
    @PostMapping("/admin/login/status")
    public R<?> loginStatus(@RequestBody AdminRequest adminRequest, HttpSession session, HttpServletRequest request) {
        String account = adminRequest.getUsername();
        if (loginAttemptService.isBlocked(account, request)) {
            long remain = loginAttemptService.getRemainingSeconds(account, request);
            return R.error("登录失败次数过多，请" + remain + "秒后重试");
        }

        R<?> result = adminService.verityPasswd(adminRequest, session);
        if (Boolean.TRUE.equals(result.getSuccess())) {
            loginAttemptService.onLoginSuccess(account, request);
        } else {
            loginAttemptService.onLoginFailure(account, request);
        }
        return result;
    }
}
