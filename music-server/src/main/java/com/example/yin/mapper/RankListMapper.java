package com.example.yin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.yin.model.domain.RankList;
import org.springframework.stereotype.Repository;

@Repository
public interface RankListMapper extends BaseMapper<RankList> {


    /**
     * 查总分
     *
     * @param songListId
     * @return
     */
    int selectScoreSum(Long songListId);

}
