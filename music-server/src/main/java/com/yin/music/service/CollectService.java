package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.Collect;
import com.yin.music.model.request.CollectRequest;

public interface CollectService extends IService<Collect> {


    R<?> addCollection(CollectRequest addCollectRequest, Integer currentUserId);

    R<?> addSongListCollection(CollectRequest addCollectRequest, Integer currentUserId);

    R<?> existSongId(CollectRequest isCollectRequest, Integer currentUserId);

    R<?> deleteCollect(Integer currentUserId, Integer songId);

    R<?> existSongListId(CollectRequest isCollectRequest, Integer currentUserId);

    R<?> deleteSongListCollect(Integer currentUserId, Integer songListId);

    R<?> collectionUsersOfSongList(Integer songListId);

    R<?> collectionOfUser(Integer userId);
}
