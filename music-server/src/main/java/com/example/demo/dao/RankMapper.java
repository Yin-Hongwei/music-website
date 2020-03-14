package com.example.demo.dao;

import com.example.demo.domain.Rank;
import org.springframework.stereotype.Repository;

@Repository
public interface RankMapper {

    int insert(Rank record);

    int insertSelective(Rank record);

    /**
     * 查总分
     * @param songListId
     * @return
     */
    int selectScoreSum(Long songListId);

    /**
     * 查总评分人数
     * @param songListId
     * @return
     */
    int selectRankNum(Long songListId);
}
