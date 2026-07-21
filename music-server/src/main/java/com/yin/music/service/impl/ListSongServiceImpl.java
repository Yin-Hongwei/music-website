package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.mapper.ListSongMapper;
import com.yin.music.mapper.SongListMapper;
import com.yin.music.mapper.SongMapper;
import com.yin.music.model.domain.ListSong;
import com.yin.music.model.domain.Song;
import com.yin.music.model.domain.SongList;
import com.yin.music.model.request.ListSongRequest;
import com.yin.music.model.response.ListSongResponse;
import com.yin.music.service.ListSongService;
import com.yin.music.service.support.SongAssembler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListSongServiceImpl extends ServiceImpl<ListSongMapper, ListSong> implements ListSongService {

    private final ListSongMapper listSongMapper;

    private final SongMapper songMapper;

    private final SongListMapper songListMapper;

    private final SongAssembler songAssembler;

    @Override
    public List<ListSong> allListSong() {
        return listSongMapper.selectList(null);
    }

    @Override
    public R<?> updateListSongMsg(ListSongRequest updateListSongRequest) {
        ListSong listSong = new ListSong();
        BeanUtils.copyProperties(updateListSongRequest, listSong);
        if (listSongMapper.updateById(listSong) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R<?> deleteListSong(Integer songId, Integer songListId) {
        if (songId == null || songListId == null) {
            return R.error("歌曲 ID 和歌单 ID 不能为空");
        }
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_id", songId).eq("song_sheet_id", songListId);
        if (listSongMapper.delete(queryWrapper) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败，该歌单中不存在此歌曲");
        }
    }

    @Override
    public R<?> addListSong(ListSongRequest addListSongRequest) {
        Integer songId = addListSongRequest.getSongId();
        Integer songListId = addListSongRequest.getSongListId();
        if (songId == null || songListId == null) {
            return R.error("歌曲 ID 和歌单 ID 不能为空");
        }

        Song song = songMapper.selectById(songId);
        if (song == null) {
            return R.error("歌曲不存在");
        }
        SongList songList = songListMapper.selectById(songListId);
        if (songList == null) {
            return R.error("歌单不存在");
        }

        QueryWrapper<ListSong> existsQuery = new QueryWrapper<>();
        existsQuery.eq("song_id", songId).eq("song_sheet_id", songListId);
        if (listSongMapper.selectCount(existsQuery) > 0) {
            return R.error("该歌曲已在歌单中");
        }

        ListSong listSong = new ListSong();
        listSong.setSongId(songId);
        listSong.setSongListId(songListId);
        if (listSongMapper.insert(listSong) > 0) {
            return R.success("添加成功", Map.of("id", listSong.getId()));
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R<?> listSongOfSongId(Integer songListId) {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_sheet_id", songListId);
        return R.success("查询成功", ListSongResponse.fromList(listSongMapper.selectList(queryWrapper)));
    }

    @Override
    public R<?> songDetailOfSongListId(Integer songListId) {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_sheet_id", songListId);
        List<ListSong> listSongList = listSongMapper.selectList(queryWrapper);
        if (listSongList == null || listSongList.isEmpty()) {
            return R.success("查询成功", Collections.emptyList());
        }

        List<Integer> songIds = listSongList.stream()
                .map(ListSong::getSongId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (songIds.isEmpty()) {
            return R.success("查询成功", Collections.emptyList());
        }

        Map<Integer, Song> songMap = songMapper.selectBatchIds(songIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Song::getId, Function.identity(), (a, b) -> a));

        List<Song> orderedSongs = new ArrayList<>();
        for (ListSong listSong : listSongList) {
            Song song = songMap.get(listSong.getSongId());
            if (song != null) {
                orderedSongs.add(song);
            }
        }
        return R.success("查询成功", songAssembler.toDetailList(orderedSongs));
    }

}
