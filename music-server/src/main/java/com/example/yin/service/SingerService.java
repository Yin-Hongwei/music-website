package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.domain.Singer;

import java.util.List;

public interface SingerService {

    boolean addSinger (Singer singer);

    boolean updateSingerMsg(Singer singer);

    boolean updateSingerPic(Singer singer);

    R deleteSinger(Integer id);

    R allSinger();

    List<Singer> singerOfName(String name);

    R singerOfSex(Integer sex);
}
