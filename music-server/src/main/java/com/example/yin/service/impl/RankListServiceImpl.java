package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.RankListMapper;
import com.example.yin.model.domain.RankList;
import com.example.yin.model.request.RankListRequest;
import com.example.yin.service.RankListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class RankListServiceImpl implements RankListService {


    @Autowired
    private RankListMapper rankMapper;

    @Override
    public R addRank(RankListRequest rankListAddRequest) {
        RankList rankList = new RankList();
        BeanUtils.copyProperties(rankListAddRequest,rankList);
        if (rankMapper.insertSelective(rankList) > 0) {
            return R.success("评价成功");
        } else {
            return R.error("评价失败");
        }
    }

    @Override
    public R rankOfSongListId(Long songListId) {
        // 评分总人数如果为 0，则返回0；否则返回计算出的结果
        int rankNum = rankMapper.selectRankNum(songListId);
        return R.success(null, (rankNum <= 0) ? 0 : rankMapper.selectScoreSum(songListId) / rankNum);
    }

    @Override
    public R getUserRank(Long consumerId, Long songListId) {
        return R.success(null, rankMapper.selectUserRank(consumerId, songListId));
    }
}
