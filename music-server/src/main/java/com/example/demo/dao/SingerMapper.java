package com.example.demo.dao;

import com.example.demo.domain.Singer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Singer record);

    int insertSelective(Singer record);

    Singer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Singer record);

    int updateByPrimaryKey(Singer record);

    int updateSingerMsg(Singer record);

    int updateSingerImg(Singer record);

    int deleteSinger(Integer id);

    List<Singer> listSingers();

    List<Singer> searachSinger(String name);

    List<Singer> singerSex(Integer sex);
}
