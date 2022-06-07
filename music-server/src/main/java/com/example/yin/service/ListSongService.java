package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.domain.ListSong;
import com.example.yin.model.request.ListSongRequest;

import java.util.List;

public interface ListSongService {

    R addListSong(ListSongRequest addListSongRequest);

    R updateListSongMsg(ListSongRequest updateListSongRequest);

    R deleteListSong(Integer songId);

    //看看这啥
    List<ListSong> allListSong();

    R listSongOfSongId(Integer songListId);
}
