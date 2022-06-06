package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.SongListMapper;
import com.example.yin.domain.SongList;
import com.example.yin.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongListServiceImpl implements SongListService {

    @Autowired
    private SongListMapper songListMapper;

    @Override
    public boolean updateSongListMsg(SongList songList) {
        return songListMapper.updateSongListMsg(songList) > 0 ? true : false;
    }

    @Override
    public boolean deleteSongList(Integer id) {
        return songListMapper.deleteSongList(id) > 0 ? true : false;
    }

    @Override
    public R allSongList() {
        return R.success(null, songListMapper.allSongList());
    }

    @Override
    public R likeTitle(String title) {
        return R.success(null, songListMapper.likeTitle(title));
    }

    @Override
    public R likeStyle(String style) {
        return R.success(null, songListMapper.likeStyle(style));
    }

    @Override
    public boolean addSongList(SongList songList) {
        return songListMapper.insertSelective(songList) > 0 ? true : false;
    }

    @Override
    public boolean updateSongListImg(SongList songList) {

        return songListMapper.updateSongListImg(songList) > 0 ? true : false;
    }
}
