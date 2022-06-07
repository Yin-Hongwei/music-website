package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.domain.ListSong;

import java.util.List;

public interface ListSongService {

    boolean addListSong(ListSong listSong);

    boolean updateListSongMsg(ListSong listSong);

    boolean deleteListSong(Integer songId);

    List<ListSong> allListSong();

    R listSongOfSongId(Integer songListId);
}
