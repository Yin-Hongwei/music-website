package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.AppUser;
import com.yin.music.model.request.AppUserRequest;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface AppUserService extends IService<AppUser> {

    R<?> addUser(AppUserRequest registryRequest);

    R<?> updateUserMsg(AppUserRequest updateRequest, Integer currentUserId);

    R<?> updateUserAvatar(MultipartFile avatarFile, Integer currentUserId);

    R<?> updatePassword(AppUserRequest updatePasswordRequest, Integer currentUserId);

    boolean existUser(String username);

    boolean verityPasswd(String username, String password);

    R<?> deleteUser(Integer id);

    R<?> allUser();

    R<?> pageUser(Integer page, Integer size);

    R<?> userOfId(Integer id, Integer viewerId, boolean asAdmin);

    R<?> loginStatus(AppUserRequest loginRequest, HttpServletRequest request);
}
