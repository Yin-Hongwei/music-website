package com.example.demo.service.impl;

import com.example.demo.dao.CollectMapper;
import com.example.demo.domain.Collect;
import com.example.demo.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public boolean addCollection(Collect collect) {
        return collectMapper.addCollection(collect) > 0 ? true:false;
    }

    @Override
    public boolean existSongId(Integer songId) {
        return collectMapper.existSongId(songId)>0 ? true:false;
    }

    @Override
    public boolean updateCollectMsg(Collect collect) {
        return collectMapper.updateCollectMsg(collect) >0 ?true:false;
    }

    @Override
    public boolean deleteCollect(Integer id) {
        return collectMapper.deleteCollect(id) >0 ?true:false;
    }

    @Override
    public List<Collect> allCollect()

    {
        return collectMapper.allCollect();
    }

    @Override
    public List<Collect> myCollectOfSongs(Integer userId)

    {
        return collectMapper.myCollectOfSongs(userId);
    }
}
