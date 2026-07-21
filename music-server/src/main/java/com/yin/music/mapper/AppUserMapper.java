package com.yin.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.music.model.domain.AppUser;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {
}
