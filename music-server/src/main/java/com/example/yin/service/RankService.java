package com.example.yin.service;

import com.example.yin.domain.Rank;

public interface RankService {

    int rankOfSongListId(Long songListId);

    boolean addRank(Rank rank);
}
