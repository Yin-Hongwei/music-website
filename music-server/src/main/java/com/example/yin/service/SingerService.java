package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.domain.Singer;
import com.example.yin.request.SingerRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SingerService {

    R addSinger (SingerRequest addSingerRequest);

    R updateSingerMsg(SingerRequest updateSingerRequest);

    R updateSingerPic(MultipartFile avatorFile,int id);

    R deleteSinger(Integer id);

    R allSinger();

    List<Singer> singerOfName(String name);

    R singerOfSex(Integer sex);
}
