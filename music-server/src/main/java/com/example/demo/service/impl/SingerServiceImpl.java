package com.example.demo.service.impl;

import com.example.demo.dao.SingerMapper;
import com.example.demo.domain.Singer;
import com.example.demo.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingerServiceImpl implements SingerService{

    @Autowired
    private SingerMapper singerMapper;

    @Override
    public boolean updateSingerMsg(Singer singer) {
        return singerMapper.updateSingerMsg(singer) >0 ?true:false;
    }

    @Override
    public boolean updateSingerPic(Singer singer) {

        return singerMapper.updateSingerPic(singer) >0 ?true:false;
    }

    @Override
    public boolean deleteSinger(Integer id) {
        return singerMapper.deleteSinger(id) >0 ?true:false;
    }

    @Override
    public List<Singer> allSinger()
    {
        return singerMapper.allSinger();
    }

    @Override
    public boolean addSinger(Singer singer)  {

        return singerMapper.insertSelective(singer) > 0 ? true : false;
    }

    @Override
    public List<Singer> singerOfName(String name)

    {
        return singerMapper.singerOfName(name);
    }

    @Override
    public List<Singer> singerOfSex(Integer sex)

    {
        return singerMapper.singerOfSex(sex);
    }
}
