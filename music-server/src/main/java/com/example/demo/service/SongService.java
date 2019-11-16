package com.example.demo.service;

import com.example.demo.domain.Song;

import java.util.List;

public interface SongService {

    boolean ifAdd (Song song);

    boolean updateSongMsg(Song song);

    boolean updateSongUrl(Song song);

    boolean updateSongPic(Song song);

    boolean deleteSong(Integer id);

    List<Song> listSongs();

    List<Song> listSongsOfSinger(Integer singerId);

    List<Song> listSongsOfSongs(Integer id);

    List<Song> searachSongLists(String name);

    List<Song> songOfName(String name);
}
