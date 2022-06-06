package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.CollectMapper;
import com.example.yin.domain.Collect;
import com.example.yin.request.CollectRequest;
import com.example.yin.service.CollectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public R addCollection(CollectRequest addCollectRequest) {
        //作者用type来判断收藏的是歌还是歌单
        Collect collect = new Collect();
        BeanUtils.copyProperties(addCollectRequest, collect);
        collect.setCreateTime(new Date());
        if (collectMapper.insertSelective(collect) > 0) {
            return R.success("收藏成功", true);
        } else {
            return R.error("收藏失败");
        }
    }

    @Override
    public R existSongId(CollectRequest isCollectRequest) {

        if (collectMapper.existSongId(isCollectRequest.getUserId(), isCollectRequest.getSongId()) > 0) {
            return R.success("已收藏", true);
        } else {
            return R.success("未收藏", false);
        }
    }

    @Override
    public R deleteCollect(Integer userId, Integer songId) {
        if (collectMapper.deleteCollect(userId, songId) > 0) {
            return R.success("取消收藏", false);
        } else {
            return R.error("取消收藏失败");
        }
    }

    @Override
    public R collectionOfUser(Integer userId) {
        return R.success("取消收藏", collectMapper.collectionOfUser(userId));
    }
}
