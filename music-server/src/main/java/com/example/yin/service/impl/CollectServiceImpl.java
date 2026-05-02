package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.CollectMapper;
import com.example.yin.mapper.AppUserMapper;
import com.example.yin.mapper.SongMapper;
import com.example.yin.mapper.SongListMapper;
import com.example.yin.model.domain.AppUser;
import com.example.yin.model.domain.Collect;
import com.example.yin.model.domain.Song;
import com.example.yin.model.domain.SongList;
import com.example.yin.model.request.CollectRequest;
import com.example.yin.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    private final CollectMapper collectMapper;
    private final AppUserMapper appUserMapper;
    private final SongMapper songMapper;
    private final SongListMapper songListMapper;

    @Override
    public R addCollection(CollectRequest addCollectRequest) {
        //作者用type来判断收藏的是歌还是歌单
        Collect collect = new Collect();
        BeanUtils.copyProperties(addCollectRequest, collect);
        if (collectMapper.insert(collect) > 0) {
            return R.success("收藏成功", true);
        } else {
            return R.error("收藏失败");
        }
    }

    @Override
    public R addSongListCollection(CollectRequest addCollectRequest) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", addCollectRequest.getUserId());
        queryWrapper.eq("song_sheet_id", addCollectRequest.getSongListId());
        queryWrapper.eq("type", 1);
        if (collectMapper.selectCount(queryWrapper) > 0) {
            return R.success("已收藏", true);
        }

        Collect collect = new Collect();
        collect.setUserId(addCollectRequest.getUserId());
        collect.setType((byte) 1);
        collect.setSongListId(addCollectRequest.getSongListId());
        collect.setSongId(null);
        if (collectMapper.insert(collect) > 0) {
            return R.success("收藏成功", true);
        } else {
            return R.error("收藏失败");
        }
    }

    @Override
    public R existSongId(CollectRequest isCollectRequest) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",isCollectRequest.getUserId());
        queryWrapper.eq("song_id",isCollectRequest.getSongId());
        queryWrapper.eq("type", 0);
        if (collectMapper.selectCount(queryWrapper) > 0) {
            return R.success("已收藏", true);
        } else {
            return R.success("未收藏", false);
        }
    }

    @Override
    public R deleteCollect(Integer userId, Integer songId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("song_id",songId);
        queryWrapper.eq("type", 0);
        if (collectMapper.delete(queryWrapper) > 0) {
            return R.success("取消收藏", false);
        } else {
            return R.error("取消收藏失败");
        }
    }

    @Override
    public R existSongListId(CollectRequest isCollectRequest) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", isCollectRequest.getUserId());
        queryWrapper.eq("song_sheet_id", isCollectRequest.getSongListId());
        queryWrapper.eq("type", 1);
        if (collectMapper.selectCount(queryWrapper) > 0) {
            return R.success("已收藏", true);
        } else {
            return R.success("未收藏", false);
        }
    }

    @Override
    public R deleteSongListCollect(Integer userId, Integer songListId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("song_sheet_id", songListId);
        queryWrapper.eq("type", 1);
        if (collectMapper.delete(queryWrapper) > 0) {
            return R.success("取消收藏", false);
        } else {
            return R.error("取消收藏失败");
        }
    }

    @Override
    public R collectionOfUser(Integer userId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByAsc("create_time");
        List<Collect> collects = collectMapper.selectList(queryWrapper);

        List<Integer> songIdsInOrder = collects.stream()
                .filter(c -> c.getType() != null && c.getType() == 0 && c.getSongId() != null)
                .map(Collect::getSongId)
                .collect(Collectors.toList());

        List<Integer> songListIdsInOrder = collects.stream()
                .filter(c -> c.getType() != null && c.getType() == 1 && c.getSongListId() != null)
                .map(Collect::getSongListId)
                .collect(Collectors.toList());

        List<Song> songs = orderByIdList(
                songIdsInOrder,
                songIdsInOrder.isEmpty()
                        ? Collections.emptyList()
                        : songMapper.selectBatchIds(songIdsInOrder),
                Song::getId
        );

        List<SongList> songSheets = orderByIdList(
                songListIdsInOrder,
                songListIdsInOrder.isEmpty()
                        ? Collections.emptyList()
                        : songListMapper.selectBatchIds(songListIdsInOrder),
                SongList::getId
        );

        Map<String, Object> data = new HashMap<>();
        data.put("songs", songs);
        data.put("songSheets", songSheets);
        return R.success("用户收藏", data);
    }

    private static <T> List<T> orderByIdList(
            List<Integer> idOrder,
            List<T> rows,
            Function<T, Integer> idGetter
    ) {
        if (idOrder.isEmpty() || rows.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, T> byId = rows.stream().collect(Collectors.toMap(idGetter, Function.identity(), (a, b) -> a));
        List<T> ordered = new ArrayList<>();
        for (Integer id : idOrder) {
            T row = byId.get(id);
            if (row != null) {
                ordered.add(row);
            }
        }
        return ordered;
    }

    @Override
    public R collectionUsersOfSongList(Integer songListId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("song_sheet_id", songListId);
        queryWrapper.eq("type", 1);
        List<Collect> collectList = collectMapper.selectList(queryWrapper);
        if (collectList == null || collectList.isEmpty()) {
            Map<String, Object> data = new HashMap<>();
            data.put("total", 0);
            data.put("users", Collections.emptyList());
            return R.success("收藏者列表", data);
        }

        List<Integer> userIds = collectList.stream()
                .map(Collect::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<AppUser> users = userIds.isEmpty()
                ? Collections.emptyList()
                : appUserMapper.selectBatchIds(userIds);

        Map<String, Object> data = new HashMap<>();
        data.put("total", users.size());
        data.put("users", users);
        return R.success("收藏者列表", data);
    }
}
