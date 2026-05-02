package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.CollectRequest;
import com.example.yin.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CollectController {

    private final CollectService collectService;


    // 添加收藏的歌曲
    //前台界面逻辑
    @PostMapping("/collection/add")
    public R addCollection(@RequestBody CollectRequest addCollectRequest) {
        return collectService.addCollection(addCollectRequest);
    }

    // 添加收藏的歌单
    @PostMapping("/collection/songList/add")
    public R addSongListCollection(@RequestBody CollectRequest addCollectRequest) {
        return collectService.addSongListCollection(addCollectRequest);
    }

    //TODO  这些其实有点偏简单的逻辑  所以就一点 所以放在外面  拿到里面
    // 取消收藏的歌曲
    @DeleteMapping("/collection/delete")
    public R deleteCollection(@RequestParam Integer userId, @RequestParam Integer songId) {
        return collectService.deleteCollect(userId, songId);
    }

    // 取消收藏的歌单
    @DeleteMapping("/collection/songList/delete")
    public R deleteSongListCollection(@RequestParam Integer userId, @RequestParam Integer songListId) {
        return collectService.deleteSongListCollect(userId, songListId);
    }

    // 是否收藏歌曲
    @PostMapping("/collection/status")
    public R isCollection(@RequestBody CollectRequest isCollectRequest) {
        return collectService.existSongId(isCollectRequest);

    }

    // 是否收藏歌单
    @PostMapping("/collection/songList/status")
    public R isSongListCollection(@RequestBody CollectRequest isCollectRequest) {
        return collectService.existSongListId(isCollectRequest);
    }

    // 返回的指定用户 ID 收藏的列表
    @GetMapping("/collection/detail")
    public R collectionOfUser(@RequestParam Integer userId) {
        return collectService.collectionOfUser(userId);
    }

    // 返回收藏指定歌单的用户列表和总数
    @GetMapping("/collection/songList/collectors")
    public R collectionUsersOfSongList(@RequestParam Integer songListId) {
        return collectService.collectionUsersOfSongList(songListId);
    }
}
