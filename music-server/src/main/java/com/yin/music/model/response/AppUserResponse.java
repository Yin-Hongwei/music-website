package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.AppUser;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AppUserResponse {

    private Integer id;
    private String username;
    private Byte sex;
    private String phoneNum;
    private String email;
    private Date birth;
    private String introduction;
    private String location;
    private String avatar;
    private Date createTime;
    private Date updateTime;

    public static AppUserResponse from(AppUser source) {
        if (source == null) {
            return null;
        }
        AppUserResponse target = new AppUserResponse();
        BeanUtils.copyProperties(source, target, "password");
        return target;
    }

    public static List<AppUserResponse> fromList(List<AppUser> sources) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream().map(AppUserResponse::from).collect(Collectors.toList());
    }
}
