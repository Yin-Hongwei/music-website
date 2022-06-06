package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.request.RankListRequest;

public interface RankListService {

    R addRank(RankListRequest rankListAddRequest);

    R rankOfSongListId(Long songListId);

    R getUserRank(Long consumerId, Long songListId);

}
