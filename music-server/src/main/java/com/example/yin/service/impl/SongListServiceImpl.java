package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.SongListMapper;
import com.example.yin.mapper.SongListStyleMapper;
import com.example.yin.model.domain.SongList;
import com.example.yin.model.domain.SongListStyle;
import com.example.yin.model.request.SongListRequest;
import com.example.yin.service.MinioUploadService;
import com.example.yin.service.SongListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SongListServiceImpl extends ServiceImpl<SongListMapper, SongList> implements SongListService {

    private final SongListMapper songListMapper;

    private final SongListStyleMapper songListStyleMapper;

    private final MinioUploadService minioUploadService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public R updateSongListMsg(SongListRequest updateSongListRequest) {
        SongList songList = new SongList();
        BeanUtils.copyProperties(updateSongListRequest, songList);
        if (songListMapper.updateById(songList) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R deleteSongList(Integer id) {
        if (songListMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R allSongList() {
        return R.success(null, songListMapper.selectList(null));
    }

    @Override
    public R pageSongList(Integer page, Integer size) {
        Page<SongList> pageInfo = new Page<>(page, size);
        songListMapper.selectPage(pageInfo, null);
        return R.success(null, buildPagedData(pageInfo, page, size));
    }

    @Override
    public R songListOfId(Integer id) {
        SongList songList = songListMapper.selectById(id);
        if (songList == null) {
            return R.error("歌单不存在");
        }
        return R.success(null, songList);
    }

    @Override
    public R likeTitle(String title) {
        QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",title);
        return R.success(null, songListMapper.selectList(queryWrapper));
    }

    @Override
    public R likeStyle(String style) {
        QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("style",style);
        return R.success(null, songListMapper.selectList(queryWrapper));
    }

    @Override
    public R pageLikeStyle(String style, Integer page, Integer size) {
        QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("style", style);
        Page<SongList> pageInfo = new Page<>(page, size);
        songListMapper.selectPage(pageInfo, queryWrapper);
        return R.success(null, buildPagedData(pageInfo, page, size));
    }

    @Override
    public R allSongListStyle() {
        List<Map<String, Object>> styleList = new ArrayList<>();
        Map<String, Object> allStyle = new HashMap<>();
        allStyle.put("id", 0);
        allStyle.put("name", "全部歌单");
        styleList.add(allStyle);

        List<SongListStyle> dbStyles = songListStyleMapper.selectList(new QueryWrapper<SongListStyle>().orderByAsc("id"));
        for (SongListStyle item : dbStyles) {
            if (item == null || item.getName() == null || item.getName().trim().isEmpty()) {
                continue;
            }
            Map<String, Object> styleItem = new HashMap<>();
            styleItem.put("id", item.getId());
            styleItem.put("name", item.getName().trim());
            styleList.add(styleItem);
        }
        return R.success(null, styleList);
    }

    @Override
    public R recommendSongList(Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? 10 : Math.min(limit, 50);
        return R.success(null, songListMapper.selectRecommendedSongLists(safeLimit));
    }

    @Override
    public R addSongList(SongListRequest addSongListRequest) {
        SongList songList = new SongList();
        BeanUtils.copyProperties(addSongListRequest, songList);
        String pic = "/img/songListPic/123.jpg";
        songList.setPic(pic);
        if (songListMapper.insert(songList) > 0) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R updateSongListImg(MultipartFile avatorFile, @RequestParam("id") int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        if (!minioUploadService.uploadSongListImg(avatorFile, fileName)) {
            return R.fatal("上传失败");
        }
        String imgPath = "/" + bucketName + "/songlist/" + fileName;
        SongList songList = new SongList();
        songList.setId(id);
        songList.setPic(imgPath);
        if (songListMapper.updateById(songList) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", imgPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    private java.util.Map<String, Object> buildPagedData(Page<SongList> pageInfo, Integer page, Integer size) {
        java.util.Map<String, Object> data = new HashMap<>();
        data.put("records", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("hasMore", page * size < pageInfo.getTotal());
        return data;
    }
}
