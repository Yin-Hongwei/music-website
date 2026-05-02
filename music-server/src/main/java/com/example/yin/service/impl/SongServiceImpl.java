package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.SongMapper;
import com.example.yin.model.domain.Song;
import com.example.yin.model.request.SongRequest;
import com.example.yin.service.MinioUploadService;
import com.example.yin.service.SongService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {

    private final SongMapper songMapper;

    private final MinioUploadService minioUploadService;

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public R allSong() {
        return R.success(null, songMapper.selectList(null));
    }

    @Override
    public R pageSong(Integer page, Integer size) {
        Page<Song> pageInfo = new Page<>(page, size);
        songMapper.selectPage(pageInfo, null);
        Map<String, Object> data = new HashMap<>();
        data.put("records", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("hasMore", page * size < pageInfo.getTotal());
        return R.success(null, data);
    }

    @Override
    public R addSong(SongRequest addSongRequest, MultipartFile mpfile) {
        Song song = new Song();
        BeanUtils.copyProperties(addSongRequest, song);
        String pic = "/img/songPic/tubiao.jpg";
        String fileName = mpfile.getOriginalFilename();
        if (!minioUploadService.uploadSongFile(mpfile)) {
            return R.fatal("上传失败");
        }
        String storeUrlPath = "/" + bucketName + "/" + fileName;
        song.setCreateTime(new Date());
        song.setUpdateTime(new Date());
        song.setPic(pic);
        song.setUrl(storeUrlPath);
        song.setDuration(sanitizeDuration(song.getDuration()));
        if (songMapper.insert(song) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", storeUrlPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R updateSongMsg(SongRequest updateSongRequest) {
        Song song = new Song();
        BeanUtils.copyProperties(updateSongRequest, song);
        if (songMapper.updateById(song) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updateSongUrl(MultipartFile urlFile, int id) {
        Song existing = songMapper.selectById(id);
        if (existing != null && existing.getUrl() != null) {
            String prefix = "/" + bucketName + "/";
            if (existing.getUrl().startsWith(prefix)) {
                String oldObject = existing.getUrl().substring(prefix.length());
                if (!oldObject.contains("/")) {
                    try {
                        minioClient.removeObject(
                                RemoveObjectArgs.builder().bucket(bucketName).object(oldObject).build());
                    } catch (Exception e) {
                        log.warn("MinIO removeObject skipped for {}: {}", oldObject, e.getMessage());
                    }
                }
            }
        }
        String fileName = urlFile.getOriginalFilename();
        if (!minioUploadService.uploadSongFile(urlFile)) {
            return R.fatal("更新失败");
        }
        String storeUrlPath = "/" + bucketName + "/" + fileName;
        Song song = new Song();
        song.setId(id);
        song.setUrl(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", storeUrlPath);
            return R.success("更新成功", data);
        } else {
            return R.error("更新失败");
        }
    }

    @Override
    public R updateSongPic(MultipartFile urlFile, int id) {
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        if (!minioUploadService.uploadSongCoverImg(urlFile, fileName)) {
            return R.fatal("上传失败");
        }
        String storeUrlPath = "/" + bucketName + "/singer/song/" + fileName;
        Song song = new Song();
        song.setId(id);
        song.setPic(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", storeUrlPath);
            return R.success("上传成功", data);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R deleteSong(Integer id) {
        Song song = songMapper.selectById(id);
        if (songMapper.deleteById(id) > 0) {
            if (song != null && song.getUrl() != null) {
                String prefix = "/" + bucketName + "/";
                if (song.getUrl().startsWith(prefix)) {
                    String objectName = song.getUrl().substring(prefix.length());
                    if (!objectName.contains("/")) {
                        try {
                            minioClient.removeObject(
                                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
                        } catch (Exception e) {
                            log.warn("MinIO removeObject skipped for {}: {}", objectName, e.getMessage());
                        }
                    }
                }
            }
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R songOfSingerId(Integer singerId) {
        try {
            QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("singer_id", singerId);
            java.util.List<Song> songs = songMapper.selectList(queryWrapper);
            log.info("Query songs by singerId={}, count={}", singerId, songs != null ? songs.size() : 0);
            if (songs != null && !songs.isEmpty()) {
                return R.success("查询成功", songs);
            } else {
                return R.success("查询成功，但该歌手暂无歌曲", java.util.Collections.emptyList());
            }
        } catch (Exception e) {
            log.error("Failed to query songs by singerId={}", singerId, e);
            return R.fatal("查询失败: " + e.getMessage());
        }
    }

    @Override
    public R songOfId(Integer id) {
        Song song = songMapper.selectById(id);
        return R.success(null, song);
    }

    @Override
    public R songOfSingerName(String name) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        return R.success(null, songMapper.selectList(queryWrapper));
    }

    private Integer sanitizeDuration(Integer durationSeconds) {
        if (durationSeconds == null || durationSeconds < 0) {
            return 0;
        }
        return durationSeconds;
    }

}
