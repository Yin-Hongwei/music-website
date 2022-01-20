package com.example.yin.service;

import com.example.yin.domain.RankList;

public interface RankListService {

    int rankOfSongListId(Long songListId);

    boolean addRank(RankList rankList);
}
