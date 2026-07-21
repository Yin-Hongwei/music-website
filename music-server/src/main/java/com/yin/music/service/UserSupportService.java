package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.UserSupport;
import com.yin.music.model.request.UserSupportRequest;

/**
 * @author asus
 * @description 针对表【user_support】的数据库操作Service
 * @createDate 2022-06-11 16:06:28
 */
public interface UserSupportService extends IService<UserSupport> {

    R<?> isUserSupportComment(UserSupportRequest userSupportRequest);

    R<?> insertCommentSupport(UserSupportRequest userSupportRequest);

    R<?> deleteCommentSupport(UserSupportRequest userSupportRequest);
}
