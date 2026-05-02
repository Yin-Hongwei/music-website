package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.Collect;
import com.example.yin.model.request.CollectRequest;

public interface CollectService extends IService<Collect> {

    R addCollection(CollectRequest addCollectRequest);

    R addSongListCollection(CollectRequest addCollectRequest);

    R existSongId(CollectRequest isCollectRequest);

    R deleteCollect(Integer userId,Integer songId);

    R existSongListId(CollectRequest isCollectRequest);

    R deleteSongListCollect(Integer userId, Integer songListId);

    R collectionUsersOfSongList(Integer songListId);

    R collectionOfUser(Integer userId);
}
