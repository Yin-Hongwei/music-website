package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.config.MediaProperties;
import com.yin.music.model.R;
import com.yin.music.mapper.SingerMapper;
import com.yin.music.mapper.SongMapper;
import com.yin.music.model.domain.Singer;
import com.yin.music.model.domain.Song;
import com.yin.music.model.request.SongRequest;
import com.yin.music.model.response.PageResponse;
import com.yin.music.model.response.UploadResponse;
import com.yin.music.service.SongService;
import com.yin.music.service.support.SongAssembler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {

    private static final String DEFAULT_SONG_PIC = "/img/songPic/tubiao.jpg";
    private static final String SONG_URL_PREFIX = "/song/";
    private static final String SONG_PIC_PREFIX = "/img/songPic/";

    private final SongMapper songMapper;

    private final SingerMapper singerMapper;

    private final SongAssembler songAssembler;

    private final MediaProperties mediaProperties;

    @Override
    public R<?> allSong() {
        return R.success(null, songAssembler.toResponseList(songMapper.selectList(null)));
    }

    @Override
    public R<?> pageSong(Integer page, Integer size) {
        Page<Song> pageInfo = new Page<>(page, size);
        songMapper.selectPage(pageInfo, null);
        return R.success(null, PageResponse.of(
                songAssembler.toResponseList(pageInfo.getRecords()),
                pageInfo.getTotal(),
                page,
                size
        ));
    }

    @Override
    public R<?> addSong(SongRequest addSongRequest, MultipartFile mpfile) {
        if (mpfile == null || mpfile.isEmpty()) {
            return R.error("请上传音频文件");
        }
        String fileName = uniqueFileName(mpfile.getOriginalFilename(), "audio.mp3");
        Path dest = mediaProperties.songDir().resolve(fileName);
        try {
            Files.createDirectories(dest.getParent());
            mpfile.transferTo(dest);
        } catch (IOException e) {
            log.error("Save song file failed: {}", dest, e);
            return R.fatal("上传失败");
        }

        String storeUrlPath = SONG_URL_PREFIX + fileName;
        Song song = new Song();
        BeanUtils.copyProperties(addSongRequest, song);
        song.setCreateTime(new Date());
        song.setUpdateTime(new Date());
        song.setPic(DEFAULT_SONG_PIC);
        song.setUrl(storeUrlPath);
        song.setDuration(sanitizeDuration(song.getDuration()));
        if (songMapper.insert(song) > 0) {
            Map<String, Object> data = new HashMap<>(2);
            data.put("id", song.getId());
            data.put("url", storeUrlPath);
            return R.success("上传成功", data);
        }
        deleteQuietly(dest);
        return R.error("上传失败");
    }

    @Override
    public R<?> updateSongMsg(SongRequest updateSongRequest) {
        Song song = new Song();
        BeanUtils.copyProperties(updateSongRequest, song);
        if (song.getDuration() != null) {
            song.setDuration(sanitizeDuration(song.getDuration()));
        }
        if (songMapper.updateById(song) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R<?> updateSongUrl(MultipartFile urlFile, int id, Integer duration) {
        if (urlFile == null || urlFile.isEmpty()) {
            return R.error("请上传音频文件");
        }
        Song existing = songMapper.selectById(id);
        if (existing == null) {
            return R.error("歌曲不存在");
        }

        String fileName = uniqueFileName(urlFile.getOriginalFilename(), "audio.mp3");
        Path dest = mediaProperties.songDir().resolve(fileName);
        try {
            Files.createDirectories(dest.getParent());
            urlFile.transferTo(dest);
        } catch (IOException e) {
            log.error("Update song file failed: {}", dest, e);
            return R.fatal("更新失败");
        }

        String storeUrlPath = SONG_URL_PREFIX + fileName;
        Song song = new Song();
        song.setId(id);
        song.setUrl(storeUrlPath);
        if (duration != null) {
            song.setDuration(sanitizeDuration(duration));
        }
        if (songMapper.updateById(song) > 0) {
            deleteLocalSongFile(existing.getUrl());
            return R.success("更新成功", UploadResponse.of(storeUrlPath));
        }
        deleteQuietly(dest);
        return R.error("更新失败");
    }

    @Override
    public R<?> updateSongPic(MultipartFile urlFile, int id) {
        if (urlFile == null || urlFile.isEmpty()) {
            return R.error("请上传图片文件");
        }
        if (songMapper.selectById(id) == null) {
            return R.error("歌曲不存在");
        }
        String fileName = uniqueFileName(urlFile.getOriginalFilename(), "cover.jpg");
        Path dest = mediaProperties.imgDir("songPic").resolve(fileName);
        try {
            Files.createDirectories(dest.getParent());
            urlFile.transferTo(dest);
        } catch (IOException e) {
            log.error("Update song cover failed: {}", dest, e);
            return R.fatal("上传失败");
        }
        String storeUrlPath = SONG_PIC_PREFIX + fileName;
        Song song = new Song();
        song.setId(id);
        song.setPic(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            return R.success("上传成功", UploadResponse.of(storeUrlPath));
        }
        deleteQuietly(dest);
        return R.error("上传失败");
    }

    @Override
    public R<?> deleteSong(Integer id) {
        Song song = songMapper.selectById(id);
        if (songMapper.deleteById(id) > 0) {
            if (song != null) {
                deleteLocalSongFile(song.getUrl());
            }
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R<?> songOfSingerId(Integer singerId) {
        try {
            QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("singer_id", singerId);
            List<Song> songs = songMapper.selectList(queryWrapper);
            log.info("Query songs by singerId={}, count={}", singerId, songs != null ? songs.size() : 0);
            if (songs != null && !songs.isEmpty()) {
                return R.success("查询成功", songAssembler.toResponseList(songs));
            } else {
                return R.success("查询成功，但该歌手暂无歌曲", Collections.emptyList());
            }
        } catch (Exception e) {
            log.error("Failed to query songs by singerId={}", singerId, e);
            return R.fatal("查询失败: " + e.getMessage());
        }
    }

    @Override
    public R<?> songOfId(Integer id) {
        return R.success(null, songAssembler.toResponse(songMapper.selectById(id)));
    }

    @Override
    public R<?> songOfSingerName(String name) {
        String keyword = normalizeSearchKeyword(name);
        if (keyword.isEmpty()) {
            return R.success(null, Collections.emptyList());
        }

        QueryWrapper<Singer> singerQuery = new QueryWrapper<>();
        singerQuery.like("name", keyword);
        List<Integer> singerIds = singerMapper.selectList(singerQuery).stream()
                .map(Singer::getId)
                .collect(Collectors.toList());

        QueryWrapper<Song> songQuery = new QueryWrapper<>();
        songQuery.like("name", keyword);
        if (!singerIds.isEmpty()) {
            songQuery.or().in("singer_id", singerIds);
        }
        return R.success(null, songAssembler.toResponseList(songMapper.selectList(songQuery)));
    }

    private static String normalizeSearchKeyword(String name) {
        if (name == null) {
            return "";
        }
        String keyword = name.trim();
        while (keyword.startsWith("%")) {
            keyword = keyword.substring(1);
        }
        while (keyword.endsWith("%")) {
            keyword = keyword.substring(0, keyword.length() - 1);
        }
        return keyword.trim();
    }

    private Integer sanitizeDuration(Integer durationSeconds) {
        if (durationSeconds == null || durationSeconds < 0) {
            return 0;
        }
        return durationSeconds;
    }

    /**
     * Build a filesystem-safe object name. Tomcat {@code ResourceHandler} cannot
     * reliably serve non-ASCII path segments under {@code /img/songPic/**}, so we
     * keep only a timestamp + ASCII extension (matching seed data naming).
     */
    private static String uniqueFileName(String originalName, String fallback) {
        String ext = asciiExtension(originalName, fallback);
        return System.currentTimeMillis() + ext;
    }

    private static String asciiExtension(String originalName, String fallback) {
        String name = originalName == null || originalName.isBlank() ? fallback : originalName;
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0) {
            name = name.substring(slash + 1);
        }
        if (name.isBlank() || name.contains("..")) {
            name = fallback;
        }
        int dot = name.lastIndexOf('.');
        if (dot >= 0 && dot < name.length() - 1) {
            String ext = name.substring(dot).toLowerCase();
            if (ext.matches("\\.[a-z0-9]{1,8}")) {
                return ext;
            }
        }
        int fallbackDot = fallback.lastIndexOf('.');
        if (fallbackDot >= 0) {
            return fallback.substring(fallbackDot).toLowerCase();
        }
        return ".bin";
    }

    private void deleteLocalSongFile(String url) {
        if (url == null || !url.startsWith(SONG_URL_PREFIX)) {
            return;
        }
        String fileName = url.substring(SONG_URL_PREFIX.length());
        if (fileName.isEmpty() || fileName.contains("..") || fileName.contains("/")) {
            return;
        }
        deleteQuietly(mediaProperties.songDir().resolve(fileName));
    }

    private static void deleteQuietly(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // best-effort cleanup
        }
    }

}
