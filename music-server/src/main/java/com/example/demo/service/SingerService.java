package com.example.demo.service;

import com.example.demo.domain.Singer;

import java.util.List;

public interface SingerService {

    boolean ifAdd (Singer singer);

    boolean updateSingerMsg(Singer singer);

    boolean updateSingerImg(Singer singer);

    boolean deleteSinger(Integer id);

    List<Singer> listSingers();

    List<Singer> searachSinger(String name);

    List<Singer> singerSex(Integer sex);
}
