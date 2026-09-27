package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.config.MediaProperties;
import com.yin.music.mapper.AppUserMapper;
import com.yin.music.model.domain.AppUser;
import com.yin.music.model.request.AppUserRequest;
import com.yin.music.model.response.AppUserResponse;
import com.yin.music.model.response.PageResponse;
import com.yin.music.model.response.UploadResponse;
import com.yin.music.service.AppUserService;
import com.yin.music.support.SafeFilenames;
import com.yin.music.support.SessionUser;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser>
        implements AppUserService {
    /** Default bundled avatar still lives under the legacy directory name. */
    private static final String DEFAULT_AVATAR = "/img/avatarImages/user.jpg";

    private final AppUserMapper appUserMapper;

    private final PasswordEncoder passwordEncoder;

    private final MediaProperties mediaProperties;

    /** Legacy MD5 salt; prefer BCrypt for new passwords. */
    @Value("${app.security.password-salt:zyt}")
    private String passwordSalt;

    @Value("${app.security.allow-legacy-md5:true}")
    private boolean allowLegacyMd5;

    @Override
    public R<?> addUser(AppUserRequest registryRequest) {
        if (this.existUser(registryRequest.getUsername())) {
            return R.warning("用户名已注册");
        }
        String rawPassword = registryRequest.getPassword();
        if (StringUtils.isBlank(rawPassword) || rawPassword.length() < 6) {
            return R.error("密码至少 6 位");
        }
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(registryRequest, appUser);
        String password = passwordEncoder.encode(rawPassword);
        appUser.setPassword(password);
        if (StringUtils.isBlank(appUser.getPhoneNum())) {
            appUser.setPhoneNum(null);
        }
        if ("".equals(appUser.getEmail())) {
            appUser.setEmail(null);
        }
        if (StringUtils.isBlank(appUser.getAvatar())) {
            appUser.setAvatar(DEFAULT_AVATAR);
        } else if (!appUser.getAvatar().startsWith("/")) {
            appUser.setAvatar("/" + appUser.getAvatar());
        }
        try {
            if (appUserMapper.insert(appUser) > 0) {
                return R.success("注册成功");
            } else {
                return R.error("注册失败");
            }
        } catch (DuplicateKeyException e) {
            return R.fatal(e.getMessage());
        }
    }

    @Override
    public R<?> updateUserMsg(AppUserRequest updateRequest, Integer currentUserId) {
        // updateById 默认忽略 null，清空手机号/邮箱必须用 UpdateWrapper 显式 set
        String phoneNum = StringUtils.isBlank(updateRequest.getPhoneNum()) ? null : updateRequest.getPhoneNum().trim();
        String email = StringUtils.isBlank(updateRequest.getEmail()) ? null : updateRequest.getEmail().trim();
        UpdateWrapper<AppUser> wrapper = new UpdateWrapper<>();
        // Target account must be the authenticated session user — ignore client id.
        wrapper.eq("id", currentUserId)
                .set("username", updateRequest.getUsername())
                .set("sex", updateRequest.getSex())
                .set("phone_num", phoneNum)
                .set("email", email)
                .set("birth", updateRequest.getBirth())
                .set("introduction", updateRequest.getIntroduction())
                .set("location", updateRequest.getLocation())
                .set("update_time", new Date());
        try {
            if (appUserMapper.update(null, wrapper) > 0) {
                return R.success("修改成功");
            } else {
                return R.error("修改失败");
            }
        } catch (DuplicateKeyException e) {
            return R.warning("手机号或邮箱已被其他用户使用");
        }
    }

    @Override
    public R<?> updatePassword(AppUserRequest updatePasswordRequest, Integer currentUserId) {
        AppUser currentUser = appUserMapper.selectById(currentUserId);
        if (currentUser == null) {
            return R.error("用户不存在");
        }
        // Verify old password against the session user only — never trust body id/username as target.
        if (!matchesStoredPassword(currentUser, updatePasswordRequest.getOldPassword())) {
            return R.error("密码输入错误");
        }
        String newPassword = updatePasswordRequest.getPassword();
        if (StringUtils.isBlank(newPassword) || newPassword.length() < 6) {
            return R.error("密码至少 6 位");
        }

        AppUser appUser = new AppUser();
        appUser.setId(currentUserId);
        appUser.setPassword(passwordEncoder.encode(newPassword));

        if (appUserMapper.updateById(appUser) > 0) {
            return R.success("密码修改成功");
        } else {
            return R.error("密码修改失败");
        }
    }

    @Override
    public R<?> updateUserAvatar(MultipartFile avatarFile, Integer currentUserId) {
        // Avatar URLs are /img/avatarImages/** (local media), same as banner/swiper.
        SafeFilenames.requireAllowed(avatarFile, SafeFilenames.Kind.IMAGE);
        String fileName = SafeFilenames.uniqueImage(avatarFile.getOriginalFilename(), "avatar.jpg");
        Path folder = mediaProperties.imgDir("avatarImages");
        try {
            Files.createDirectories(folder);
            avatarFile.transferTo(folder.resolve(fileName));
        } catch (IOException e) {
            return R.fatal("上传失败");
        }
        String imgPath = "/img/avatarImages/" + fileName;
        AppUser appUser = new AppUser();
        appUser.setId(currentUserId);
        appUser.setAvatar(imgPath);
        if (appUserMapper.updateById(appUser) > 0) {
            return R.success("上传成功", UploadResponse.of(imgPath));
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public boolean existUser(String username) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return appUserMapper.selectCount(queryWrapper) > 0;
    }

    private AppUser findAppUserByLoginAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        String trimmed = account.trim();
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(w -> w.eq("username", trimmed).or().eq("email", trimmed));
        return appUserMapper.selectOne(queryWrapper);
    }

    private boolean matchesStoredPassword(AppUser appUser, String password) {
        if (appUser == null || StringUtils.isBlank(appUser.getPassword())) {
            return false;
        }
        String storedPassword = appUser.getPassword();
        if (passwordEncoder.matches(password, storedPassword)) {
            return true;
        }

        if (!allowLegacyMd5) {
            return false;
        }

        String legacyPassword = DigestUtils.md5DigestAsHex(
                (passwordSalt + password).getBytes(StandardCharsets.UTF_8));
        if (legacyPassword.equals(storedPassword)) {
            AppUser update = new AppUser();
            update.setId(appUser.getId());
            update.setPassword(passwordEncoder.encode(password));
            appUserMapper.updateById(update);
            return true;
        }
        return false;
    }

    @Override
    public boolean verityPasswd(String username, String password) {
        AppUser appUser = findAppUserByLoginAccount(username);
        return matchesStoredPassword(appUser, password);
    }

    @Override
    public R<?> deleteUser(Integer id) {
        if (appUserMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R<?> allUser() {
        return R.success(null, AppUserResponse.fromList(appUserMapper.selectList(null)));
    }

    @Override
    public R<?> pageUser(Integer page, Integer size) {
        Page<AppUser> pageInfo = new Page<>(page, size);
        appUserMapper.selectPage(pageInfo, null);
        return R.success(null, PageResponse.of(
                AppUserResponse.fromList(pageInfo.getRecords()),
                pageInfo.getTotal(),
                page,
                size
        ));
    }

    @Override
    public R<?> userOfId(Integer id, Integer viewerId, boolean asAdmin) {
        AppUser appUser = appUserMapper.selectById(id);
        boolean fullAccess = asAdmin || (viewerId != null && viewerId.equals(id));
        return R.success(null, fullAccess ? AppUserResponse.from(appUser) : AppUserResponse.toPublic(appUser));
    }

    @Override
    public R<?> loginStatus(AppUserRequest loginRequest, HttpServletRequest request) {

        String account = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (!this.verityPasswd(account, password)) {
            return R.error("用户名或密码错误");
        }
        AppUser appUser = findAppUserByLoginAccount(account);
        HttpSession session = SessionUser.rotate(request);
        SessionUser.bind(session, appUser.getId(), appUser.getUsername());
        return R.success("登录成功", AppUserResponse.from(appUser));
    }
}
