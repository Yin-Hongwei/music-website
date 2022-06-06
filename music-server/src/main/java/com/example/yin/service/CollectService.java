package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.request.CollectRequest;

public interface CollectService {

    R addCollection(CollectRequest addCollectRequest);

    R existSongId(CollectRequest isCollectRequest);

    R deleteCollect(Integer userId,Integer songId);

    R collectionOfUser(Integer userId);
}
