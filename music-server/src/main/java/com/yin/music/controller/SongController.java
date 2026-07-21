package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yin.music.model.R;
import com.yin.music.model.request.SongRequest;
import com.yin.music.service.SongService;

@RequiredArgsConstructor
@RestController
public class SongController {

    private final SongService songService;

    // 添加歌曲
    @PostMapping("/song/add")
    public R<?> addSong(SongRequest addSongRequest, @RequestParam("file") MultipartFile mpfile) {
        return songService.addSong(addSongRequest, mpfile);
    }

    // 删除歌曲
    @DeleteMapping("/song/delete")
    public R<?> deleteSong(@RequestParam int id) {
        return songService.deleteSong(id);
    }

    // 返回所有歌曲
    @GetMapping("/song")
    public R<?> allSong() {
        return songService.allSong();
    }

    @GetMapping("/song/page")
    public R<?> pageSong(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return songService.pageSong(safePage, safeSize);
    }

    //TODO ok
    // 返回指定歌曲ID的歌曲
    @GetMapping("/song/detail")
    public R<?> songOfId(@RequestParam int id) {
        return songService.songOfId(id);
    }

    // 返回指定歌手ID的歌曲
    @GetMapping("/song/singer/detail")
    public R<?> songOfSingerId(@RequestParam(value = "singerId", required = true) Integer singerId) {
        if (singerId == null) {
            return R.error("singerId参数不能为空");
        }
        return songService.songOfSingerId(singerId);
    }

    // 按关键字搜索歌曲（匹配歌名或歌手名）
    @GetMapping("/song/singerName/detail")
    public R<?> songOfSingerName(@RequestParam String name) {
        return songService.songOfSingerName(name);
    }

    // 更新歌曲信息
    @PostMapping("/song/update")
    public R<?> updateSongMsg(@RequestBody SongRequest updateSongRequest) {
        return songService.updateSongMsg(updateSongRequest);
    }

    // 更新歌曲图片
    @PostMapping("/song/img/update")
    public R<?> updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongPic(urlFile, id);
    }

    // 更新歌曲音频（可选同步时长，单位：秒）
    @PostMapping("/song/url/update")
    public R<?> updateSongUrl(
            @RequestParam("file") MultipartFile urlFile,
            @RequestParam("id") int id,
            @RequestParam(value = "duration", required = false) Integer duration
    ) {
        return songService.updateSongUrl(urlFile, id, duration);
    }
}
