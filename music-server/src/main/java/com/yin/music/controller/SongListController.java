package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yin.music.model.R;
import com.yin.music.model.request.SongListRequest;
import com.yin.music.service.SongListService;

@RequiredArgsConstructor
@RestController
public class SongListController {

    private final SongListService songListService;

    @PostMapping("/songList/add")
    public R<?> addSongList(@RequestBody SongListRequest addSongListRequest) {
        return songListService.addSongList(addSongListRequest);
    }

    @GetMapping("/songList/delete")
    public R<?> deleteSongList(@RequestParam int id) {
        return songListService.deleteSongList(id);
    }

    @GetMapping("/songList")
    public R<?> allSongList() {
        return songListService.allSongList();
    }

    @GetMapping("/songList/recommend")
    public R<?> recommendSongList(@RequestParam(defaultValue = "10") int limit) {
        return songListService.recommendSongList(Math.max(limit, 1));
    }

    @GetMapping("/songList/page")
    public R<?> pageSongList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return songListService.pageSongList(Math.max(page, 1), Math.max(size, 1));
    }

    @GetMapping("/songList/detail")
    public R<?> songListOfId(@RequestParam int id) {
        return songListService.songListOfId(id);
    }

    @GetMapping("/songList/likeTitle/detail")
    public R<?> songListOfLikeTitle(@RequestParam String title) {
        return songListService.likeTitle(title);
    }

    /** 按风格 id 筛选；styleId=0 表示全部 */
    @GetMapping("/songList/style/detail")
    public R<?> songListOfStyle(@RequestParam(defaultValue = "0") int styleId) {
        return songListService.listByStyleId(styleId);
    }

    @GetMapping("/songList/style/list")
    public R<?> songListStyle() {
        return songListService.allSongListStyle();
    }

    /** 分页按风格 id 筛选；styleId=0 等同全部分页 */
    @GetMapping("/songList/style/page/detail")
    public R<?> pageSongListOfStyle(
            @RequestParam(defaultValue = "0") int styleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return songListService.pageByStyleId(styleId, Math.max(page, 1), Math.max(size, 1));
    }

    @PostMapping("/songList/update")
    public R<?> updateSongListMsg(@RequestBody SongListRequest updateSongListRequest) {
        return songListService.updateSongListMsg(updateSongListRequest);
    }

    @PostMapping("/songList/img/update")
    public R<?> updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return songListService.updateSongListImg(avatorFile, id);
    }
}
