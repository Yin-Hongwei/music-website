package com.example.yin.service.impl;

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
    public List<Song> allSong()
    {
        return songMapper.allSong();
    }

    @Override
    public boolean addSong(Song song)
    {

        return songMapper.insertSelective(song) > 0?true:false;
    }

    @Override
    public boolean updateSongMsg(Song song) {
        return songMapper.updateSongMsg(song) >0 ?true:false;
    }

    @Override
    public boolean updateSongUrl(Song song) {

        return songMapper.updateSongUrl(song) >0 ?true:false;
    }

    @Override
    public boolean updateSongPic(Song song) {

        return songMapper.updateSongPic(song) >0 ?true:false;
    }

    @Override
    public boolean deleteSong(Integer id) {
        return songMapper.deleteSong(id) >0 ?true:false;
    }

    @Override
    public List<Song> songOfSingerId(Integer singerId)

    {
        return songMapper.songOfSingerId(singerId);
    }

    @Override
    public List<Song> songOfId(Integer id)

    {
        return songMapper.songOfId(id);
    }

    @Override
    public List<Song> songOfSingerName(String name)

    {
        return songMapper.songOfSingerName(name);
    }
}
