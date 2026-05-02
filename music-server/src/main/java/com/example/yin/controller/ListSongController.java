package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.ListSongRequest;
import com.example.yin.service.ListSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ListSongController {

    private final ListSongService listSongService;


    // 给歌单添加歌曲
    @PostMapping("/listSong/add")
    public R addListSong(@RequestBody ListSongRequest addListSongRequest) {
        return listSongService.addListSong(addListSongRequest);
    }

    // 删除歌单里的歌曲
    @GetMapping("/listSong/delete")
    public R deleteListSong(@RequestParam int songId) {
        return listSongService.deleteListSong(songId);
    }

    // 返回歌单里指定歌单 ID 的歌曲
    @GetMapping("/listSong/detail")
    public R listSongOfSongId(@RequestParam int songListId) {
        return listSongService.listSongOfSongId(songListId);
    }

    // 返回歌单里指定歌单 ID 的歌曲详情
    @GetMapping("/listSong/song/detail")
    public R songDetailOfSongListId(@RequestParam int songListId) {
        return listSongService.songDetailOfSongListId(songListId);
    }

    // 更新歌单里面的歌曲信息
    @PostMapping("/listSong/update")
    public R updateListSongMsg(@RequestBody ListSongRequest updateListSongRequest) {
        return listSongService.updateListSongMsg(updateListSongRequest);
    }
}
