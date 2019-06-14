package com.example.demo.service;

import com.example.demo.domain.Collect;

import java.util.List;

public interface CollectService {

    boolean addCollection(Collect collect);

    boolean existSongId(Integer songId);

    boolean updateCollectMsg(Collect collect);

    boolean deleteCollect(Integer id);

    List<Collect> allCollect();

    List<Collect> myCollectOfSongs(Integer userId);
}
