package com.example.yin.service.impl;

import com.example.yin.dao.RankListMapper;
import com.example.yin.domain.RankList;
import com.example.yin.service.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankListServiceImpl implements RankListService {

    @Autowired
    private RankListMapper rankMapper;

    @Override
    public int rankOfSongListId(Long songListId) {
        return rankMapper.selectScoreSum(songListId) / rankMapper.selectRankNum(songListId);
    }

    @Override
    public boolean addRank(RankList rankList) {

        return rankMapper.insertSelective(rankList) > 0 ? true:false;
    }
}
