package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.yin.music.model.R;
import com.yin.music.model.request.ListSongRequest;
import com.yin.music.service.ListSongService;

@RequiredArgsConstructor
@RestController
public class ListSongController {

    private final ListSongService listSongService;

    /** 给歌单添加歌曲 */
    @PostMapping("/listSong/add")
    public R<?> addListSong(@RequestBody ListSongRequest addListSongRequest) {
        return listSongService.addListSong(addListSongRequest);
    }

    /** 从指定歌单删除歌曲（须同时传 songId + songListId，避免误删其他歌单） */
    @DeleteMapping("/listSong/delete")
    public R<?> deleteListSong(
            @RequestParam int songId,
            @RequestParam int songListId
    ) {
        return listSongService.deleteListSong(songId, songListId);
    }

    /** 返回歌单内歌曲关联（仅 songId / songListId） */
    @GetMapping("/listSong/detail")
    public R<?> listSongOfSongId(@RequestParam int songListId) {
        return listSongService.listSongOfSongId(songListId);
    }

    /** 返回歌单内歌曲详情 */
    @GetMapping("/listSong/song/detail")
    public R<?> songDetailOfSongListId(@RequestParam int songListId) {
        return listSongService.songDetailOfSongListId(songListId);
    }

    /** 更新歌单歌曲关联 */
    @PostMapping("/listSong/update")
    public R<?> updateListSongMsg(@RequestBody ListSongRequest updateListSongRequest) {
        return listSongService.updateListSongMsg(updateListSongRequest);
    }
}
