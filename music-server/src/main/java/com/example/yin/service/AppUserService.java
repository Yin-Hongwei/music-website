package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.AppUser;
import com.example.yin.model.request.AppUserRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface AppUserService extends IService<AppUser> {

    R addUser(AppUserRequest registryRequest);

    R updateUserMsg(AppUserRequest updateRequest);

    R updateUserAvatar(MultipartFile avatarFile, int id);

    R updatePassword(AppUserRequest updatePasswordRequest);

    boolean existUser(String username);

    boolean verityPasswd(String username, String password);

    R deleteUser(Integer id);

    R allUser();

    R pageUser(Integer page, Integer size);

    R userOfId(Integer id);

    R loginStatus(AppUserRequest loginRequest, HttpSession session);
}
