package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.mapper.AdminMapper;
import com.yin.music.model.domain.Admin;
import com.yin.music.model.request.AdminRequest;
import com.yin.music.service.AdminService;
import com.yin.music.support.SessionUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private final AdminMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    /** Legacy MD5 salt (new passwords use BCrypt). */
    @Value("${app.security.password-salt:zyt}")
    private String passwordSalt;

    @Value("${app.security.allow-legacy-md5:true}")
    private boolean allowLegacyMd5;

    @Override
    public R<?> verityPasswd(AdminRequest adminRequest, HttpServletRequest request) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", adminRequest.getUsername());
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null || StringUtils.isBlank(admin.getPassword())) {
            return R.error("用户名或密码错误");
        }

        String rawPassword = adminRequest.getPassword();
        String storedPassword = admin.getPassword();

        // PRIMARY: bcrypt check
        if (passwordEncoder.matches(rawPassword, storedPassword)) {
            HttpSession session = SessionUser.rotate(request);
            SessionUser.bindAdmin(session, adminRequest.getUsername());
            return R.success("登录成功");
        }

        // LEGACY FALLBACK: MD5(salt + password) — auto-upgrade to bcrypt on match (dev/migration only)
        if (allowLegacyMd5) {
            String legacyPassword = DigestUtils.md5DigestAsHex(
                    (passwordSalt + rawPassword).getBytes(StandardCharsets.UTF_8));
            if (legacyPassword.equals(storedPassword)) {
                Admin update = new Admin();
                update.setId(admin.getId());
                update.setPassword(passwordEncoder.encode(rawPassword));
                adminMapper.updateById(update);
                HttpSession session = SessionUser.rotate(request);
                SessionUser.bindAdmin(session, adminRequest.getUsername());
                return R.success("登录成功");
            }
        }

        return R.error("用户名或密码错误");
    }
}
