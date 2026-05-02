package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.ListSongMapper;
import com.example.yin.mapper.SongMapper;
import com.example.yin.model.domain.ListSong;
import com.example.yin.model.domain.Song;
import com.example.yin.model.request.ListSongRequest;
import com.example.yin.service.ListSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ListSongServiceImpl extends ServiceImpl<ListSongMapper, ListSong> implements ListSongService {

    private final ListSongMapper listSongMapper;

    private final SongMapper songMapper;

    @Override
    public List<ListSong> allListSong() {
        return listSongMapper.selectList(null);
    }

    @Override
    public R updateListSongMsg(ListSongRequest updateListSongRequest) {
        ListSong listSong = new ListSong();
        BeanUtils.copyProperties(updateListSongRequest, listSong);
        if (listSongMapper.updateById(listSong) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R deleteListSong(Integer songId) {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_id",songId);
        if (listSongMapper.delete(queryWrapper) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R addListSong(ListSongRequest addListSongRequest) {
        ListSong listSong = new ListSong();
        BeanUtils.copyProperties(addListSongRequest, listSong);
        if (listSongMapper.insert(listSong) > 0) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R listSongOfSongId(Integer songListId) {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_sheet_id", songListId);
        return R.success("查询成功", listSongMapper.selectList(queryWrapper));
    }

    @Override
    public R songDetailOfSongListId(Integer songListId) {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_sheet_id", songListId);
        List<ListSong> listSongList = listSongMapper.selectList(queryWrapper);
        List<Map<String, Object>> songList = new ArrayList<>();
        for (ListSong listSong : listSongList) {
            Song song = songMapper.selectById(listSong.getSongId());
            if (song != null) {
                String name = song.getName();
                String singerName = "";
                String songName = "";
                if (name != null) {
                    String[] splitName = name.split("-", 2);
                    singerName = splitName[0].trim();
                    songName = splitName.length > 1 ? splitName[1].trim() : splitName[0].trim();
                }

                Map<String, Object> songMap = new LinkedHashMap<>();
                songMap.put("id", song.getId());
                songMap.put("singerId", song.getSingerId());
                songMap.put("name", song.getName());
                songMap.put("songName", songName);
                songMap.put("singerName", singerName);
                songMap.put("introduction", song.getIntroduction());
                songMap.put("createTime", song.getCreateTime());
                songMap.put("updateTime", song.getUpdateTime());
                songMap.put("pic", song.getPic());
                songMap.put("lyric", song.getLyric());
                songMap.put("url", song.getUrl());
                songList.add(songMap);
            }
        }
        return R.success("查询成功", songList);
    }

}
