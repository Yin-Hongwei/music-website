package com.example.yin.service;

import com.example.yin.domain.RankList;

public interface RankListService {

    boolean addRank(RankList rankList);

    int rankOfSongListId(Long songListId);

    int getUserRank(Long consumerId, Long songListId);

}
