package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.domain.SongList;

public interface SongListService {

    boolean addSongList (SongList songList);

    boolean updateSongListMsg(SongList songList);

    boolean updateSongListImg(SongList songList);

    boolean deleteSongList(Integer id);

    R allSongList();

    R likeTitle(String title);

    R likeStyle(String style);
}
