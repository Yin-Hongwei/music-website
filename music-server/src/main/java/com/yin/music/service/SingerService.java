package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.Singer;
import com.yin.music.model.request.SingerRequest;

import org.springframework.web.multipart.MultipartFile;

public interface SingerService extends IService<Singer> {

    R<?> addSinger (SingerRequest addSingerRequest);

    R<?> updateSingerMsg(SingerRequest updateSingerRequest);

    R<?> updateSingerPic(MultipartFile avatorFile, int id);

    R<?> deleteSinger(Integer id);

    R<?> allSinger();

    R<?> pageSinger(Integer page, Integer size);

    R<?> singerOfId(Integer id);

    R<?> singerOfName(String name);

    R<?> singerOfType(Integer type);

    R<?> allSingerStyle();
}
