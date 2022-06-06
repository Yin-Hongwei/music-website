package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.AdminMapper;
import com.example.yin.request.AdminRequest;
import com.example.yin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public R verityPasswd(AdminRequest adminRequest, HttpSession session) {
        if (adminMapper.verifyPassword(adminRequest.getUsername(), adminRequest.getPassword()) > 0) {
            session.setAttribute("name", adminRequest.getUsername());
            return R.success("登录成功");
        } else {
            return R.error("用户名或密码错误");
        }
    }
}
