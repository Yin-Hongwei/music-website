package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.domain.Song;

import java.util.List;

public interface SongService {

    boolean addSong (Song song);

    boolean updateSongMsg(Song song);

    boolean updateSongUrl(Song song);

    boolean updateSongPic(Song song);

    boolean deleteSong(Integer id);

    R allSong();

    R songOfSingerId(Integer singerId);

    List<Song> songOfId(Integer id);

    R songOfSingerName(String name);
}
