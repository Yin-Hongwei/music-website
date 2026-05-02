package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.AdminMapper;
import com.example.yin.model.domain.Admin;
import com.example.yin.model.request.AdminRequest;
import com.example.yin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private final AdminMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public R verityPasswd(AdminRequest adminRequest, HttpSession session) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",adminRequest.getUsername());
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null || StringUtils.isBlank(admin.getPassword())) {
            return R.error("用户名或密码错误");
        }

        String rawPassword = adminRequest.getPassword();
        String storedPassword = admin.getPassword();
        boolean pass = passwordEncoder.matches(rawPassword, storedPassword) || rawPassword.equals(storedPassword);
        if (pass) {
            if (rawPassword.equals(storedPassword)) {
                Admin update = new Admin();
                update.setId(admin.getId());
                update.setPassword(passwordEncoder.encode(rawPassword));
                adminMapper.updateById(update);
            }
            session.setAttribute("name", adminRequest.getUsername());
            return R.success("登录成功");
        }
        return R.error("用户名或密码错误");
    }
}
