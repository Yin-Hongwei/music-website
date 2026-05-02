package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.SongListRequest;
import com.example.yin.service.SongListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class SongListController {

    private final SongListService songListService;


    // 添加歌单
    @PostMapping("/songList/add")
    public R addSongList(@RequestBody SongListRequest addSongListRequest) {
        return songListService.addSongList(addSongListRequest);
    }

    // 删除歌单
    @GetMapping("/songList/delete")
    public R deleteSongList(@RequestParam int id) {
        return songListService.deleteSongList(id);
    }

    //TODO 这块就是前端显现相应的歌单list
    // 返回所有歌单
    @GetMapping("/songList")
    public R allSongList() {
        return songListService.allSongList();
    }

    // 基于收藏数和评论数的推荐歌单
    @GetMapping("/songList/recommend")
    public R recommendSongList(@RequestParam(defaultValue = "10") int limit) {
        int safeLimit = Math.max(limit, 1);
        return songListService.recommendSongList(safeLimit);
    }

    // 分页返回歌单
    @GetMapping("/songList/page")
    public R pageSongList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return songListService.pageSongList(safePage, safeSize);
    }

    // 返回指定 ID 的歌单详情
    @GetMapping("/songList/detail")
    public R songListOfId(@RequestParam int id) {
        return songListService.songListOfId(id);
    }

    // 返回标题包含文字的歌单
    @GetMapping("/songList/likeTitle/detail")
    public R songListOfLikeTitle(@RequestParam String title) {
        return songListService.likeTitle('%' + title + '%');
    }

    // 返回指定类型的歌单
    @GetMapping("/songList/style/detail")
    public R songListOfStyle(@RequestParam String style) {
        return songListService.likeStyle('%' + style + '%');
    }

    // 返回所有歌单风格
    @GetMapping("/songList/style/list")
    public R songListStyle() {
        return songListService.allSongListStyle();
    }

    // 分页返回指定类型的歌单
    @GetMapping("/songList/style/page/detail")
    public R pageSongListOfStyle(
        @RequestParam String style,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return songListService.pageLikeStyle('%' + style + '%', safePage, safeSize);
    }

    // 更新歌单信息
    @PostMapping("/songList/update")
    public R updateSongListMsg(@RequestBody SongListRequest updateSongListRequest) {
        return songListService.updateSongListMsg(updateSongListRequest);

    }

    // 更新歌单图片
    @PostMapping("/songList/img/update")
    public R updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return songListService.updateSongListImg(avatorFile,id);
    }
}
