package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.request.AdminRequest;

import javax.servlet.http.HttpSession;

public interface AdminService {

    R verityPasswd(AdminRequest adminRequest, HttpSession session);
}
