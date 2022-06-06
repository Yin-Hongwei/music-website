package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.domain.Collect;
import com.example.yin.request.CollectRequest;

import java.util.List;

public interface CollectService {

    R addCollection(CollectRequest addCollectRequest);

    R existSongId(CollectRequest isCollectRequest);

    R deleteCollect(Integer userId,Integer songId);

    List<Collect> collectionOfUser(Integer userId);
}
