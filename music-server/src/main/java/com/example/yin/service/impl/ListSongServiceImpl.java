package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.mapper.ListSongMapper;
import com.example.yin.model.domain.ListSong;
import com.example.yin.service.ListSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSongServiceImpl implements ListSongService {

    @Autowired
    private ListSongMapper listSongMapper;

    @Override
    public List<ListSong> allListSong() {
        return listSongMapper.allListSong();
    }

    @Override
    public boolean updateListSongMsg(ListSong listSong) {
        return listSongMapper.updateListSongMsg(listSong) > 0;
    }

    @Override
    public R deleteListSong(Integer songId) {
        if (listSongMapper.deleteListSong(songId) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public boolean addListSong(ListSong listSong) {
        return listSongMapper.insertSelective(listSong) > 0;
    }

    @Override
    public R listSongOfSongId(Integer songListId) {
        return R.success("查询成功", listSongMapper.listSongOfSongId(songListId));
    }

}
