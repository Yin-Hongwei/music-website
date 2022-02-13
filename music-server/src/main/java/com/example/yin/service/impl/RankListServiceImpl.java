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
        // 评分总人数
        int rankNum = rankMapper.selectRankNum(songListId);
        if (rankNum == 0) {
            rankNum = 1;
        }
        return rankMapper.selectScoreSum(songListId) / rankNum;
    }

    @Override
    public boolean addRank(RankList rankList) {

        return rankMapper.insertSelective(rankList) > 0 ? true:false;
    }
}
