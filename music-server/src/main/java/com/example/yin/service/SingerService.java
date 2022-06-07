package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.domain.Singer;
import com.example.yin.model.request.SingerRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SingerService {

    R addSinger (SingerRequest addSingerRequest);

    boolean updateSingerMsg(Singer singer);

    R updateSingerPic(MultipartFile avatorFile, int id);

    R deleteSinger(Integer id);

    R allSinger();

    R singerOfName(String name);

    R singerOfSex(Integer sex);
}
