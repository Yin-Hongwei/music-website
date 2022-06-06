package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.SongMapper;
import com.example.yin.domain.Song;
import com.example.yin.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongMapper songMapper;

    @Override
    public R allSong() {
        return R.success(null, songMapper.allSong());
    }

    @Override
    public boolean addSong(Song song) {

        return songMapper.insertSelective(song) > 0;
    }

    @Override
    public boolean updateSongMsg(Song song) {
        return songMapper.updateSongMsg(song) > 0;
    }

    @Override
    public boolean updateSongUrl(Song song) {

        return songMapper.updateSongUrl(song) > 0;
    }

    @Override
    public boolean updateSongPic(Song song) {

        return songMapper.updateSongPic(song) > 0;
    }

    @Override
    public boolean deleteSong(Integer id) {
        return songMapper.deleteSong(id) > 0;
    }

    @Override
    public R songOfSingerId(Integer singerId) {
        return R.success(null, songMapper.songOfSingerId(singerId));
    }

    @Override
    public List<Song> songOfId(Integer id) {
        return songMapper.songOfId(id);
    }

    @Override
    public R songOfSingerName(String name) {
        return R.success(null, songMapper.songOfSingerName(name));
    }
}
