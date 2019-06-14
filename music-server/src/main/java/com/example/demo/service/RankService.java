package com.example.demo.service;

import com.example.demo.domain.Rank;

public interface RankService {

    int selectAverScore(Long songListId);

    boolean insert(Rank rank);
}
