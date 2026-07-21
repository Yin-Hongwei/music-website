package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.mapper.AppUserMapper;
import com.yin.music.mapper.CollectMapper;
import com.yin.music.mapper.SongListMapper;
import com.yin.music.mapper.SongMapper;
import com.yin.music.model.domain.AppUser;
import com.yin.music.model.domain.Collect;
import com.yin.music.model.domain.Song;
import com.yin.music.model.domain.SongList;
import com.yin.music.model.request.CollectRequest;
import com.yin.music.model.response.AppUserResponse;
import com.yin.music.model.response.CollectorsResponse;
import com.yin.music.model.response.SongListResponse;
import com.yin.music.model.response.UserCollectResponse;
import com.yin.music.service.CollectService;
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
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    private final CollectMapper collectMapper;
    private final AppUserMapper appUserMapper;
    private final SongMapper songMapper;
    private final SongListMapper songListMapper;
    private final SongAssembler songAssembler;

    @Override
    public R<?> addCollection(CollectRequest addCollectRequest) {
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
    public R<?> addSongListCollection(CollectRequest addCollectRequest) {
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
    public R<?> existSongId(CollectRequest isCollectRequest) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", isCollectRequest.getUserId());
        queryWrapper.eq("song_id", isCollectRequest.getSongId());
        queryWrapper.eq("type", 0);
        if (collectMapper.selectCount(queryWrapper) > 0) {
            return R.success("已收藏", true);
        } else {
            return R.success("未收藏", false);
        }
    }

    @Override
    public R<?> deleteCollect(Integer userId, Integer songId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("song_id", songId);
        queryWrapper.eq("type", 0);
        if (collectMapper.delete(queryWrapper) > 0) {
            return R.success("取消收藏", false);
        } else {
            return R.error("取消收藏失败");
        }
    }

    @Override
    public R<?> existSongListId(CollectRequest isCollectRequest) {
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
    public R<?> deleteSongListCollect(Integer userId, Integer songListId) {
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
    public R<?> collectionOfUser(Integer userId) {
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

        return R.success("用户收藏", UserCollectResponse.of(
                songAssembler.toResponseList(songs),
                SongListResponse.fromList(songSheets)
        ));
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
    public R<?> collectionUsersOfSongList(Integer songListId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper();
        queryWrapper.eq("song_sheet_id", songListId);
        queryWrapper.eq("type", 1);
        List<Collect> collectList = collectMapper.selectList(queryWrapper);
        if (collectList == null || collectList.isEmpty()) {
            return R.success("收藏者列表", CollectorsResponse.of(Collections.emptyList()));
        }

        List<Integer> userIds = collectList.stream()
                .map(Collect::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<AppUser> users = userIds.isEmpty()
                ? Collections.emptyList()
                : appUserMapper.selectBatchIds(userIds);

        return R.success("收藏者列表", CollectorsResponse.of(AppUserResponse.fromList(users)));
    }
}
