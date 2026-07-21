package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.ListSong;
import com.yin.music.model.request.ListSongRequest;

import java.util.List;

public interface ListSongService extends IService<ListSong> {

    R<?> addListSong(ListSongRequest addListSongRequest);

    R<?> updateListSongMsg(ListSongRequest updateListSongRequest);

    R<?> deleteListSong(Integer songId, Integer songListId);

    List<ListSong> allListSong();

    R<?> listSongOfSongId(Integer songListId);

    R<?> songDetailOfSongListId(Integer songListId);
}
