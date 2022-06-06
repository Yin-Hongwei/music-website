package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.domain.ListSong;
import com.example.yin.request.ListSongRequest;

public interface ListSongService {

    R addListSong(ListSongRequest addListSongRequest);

    boolean updateListSongMsg(ListSong listSong);

    R deleteListSong(Integer songId);

    R listSongOfSongId(Integer songListId);
}
