package com.example.demo.service;

import com.example.demo.domain.SongList;

import java.util.List;

public interface SongListService {

    boolean ifAdd (SongList songList);

    boolean updateSongListMsg(SongList songList);

    boolean updateSongListImg(SongList songList);

    boolean deleteSongList(Integer id);

    List<SongList> listSongLists();

    List<SongList> songAlbum(String title);

}
