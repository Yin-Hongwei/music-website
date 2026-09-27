package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.UserSupport;
import com.yin.music.model.request.UserSupportRequest;

public interface UserSupportService extends IService<UserSupport> {

    R<?> isUserSupportComment(UserSupportRequest userSupportRequest, Integer currentUserId);

    R<?> insertCommentSupport(UserSupportRequest userSupportRequest, Integer currentUserId);

    R<?> deleteCommentSupport(UserSupportRequest userSupportRequest, Integer currentUserId);
}
