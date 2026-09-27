package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.yin.music.model.R;
import com.yin.music.model.request.CollectRequest;
import com.yin.music.service.CollectService;
import com.yin.music.exception.UnauthorizedException;
import com.yin.music.support.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class CollectController {

    private final CollectService collectService;


    // 添加收藏的歌曲
    //前台界面逻辑
    @PostMapping("/collection/add")
    public R<?> addCollection(@RequestBody CollectRequest addCollectRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return collectService.addCollection(addCollectRequest, currentUserId);
    }

    // 添加收藏的歌单
    @PostMapping("/collection/songList/add")
    public R<?> addSongListCollection(@RequestBody CollectRequest addCollectRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return collectService.addSongListCollection(addCollectRequest, currentUserId);
    }

    //TODO  这些其实有点偏简单的逻辑  所以就一点 所以放在外面  拿到里面
    // 取消收藏的歌曲 — app user: session only; admin (manage): may pass target userId
    @DeleteMapping("/collection/delete")
    public R<?> deleteCollection(@RequestParam Integer userId, @RequestParam Integer songId, HttpSession session) {
        Integer targetUserId = SessionUser.resolveMutableUserId(session, userId);
        return collectService.deleteCollect(targetUserId, songId);
    }

    // 取消收藏的歌单
    @DeleteMapping("/collection/songList/delete")
    public R<?> deleteSongListCollection(@RequestParam Integer userId, @RequestParam Integer songListId, HttpSession session) {
        Integer targetUserId = SessionUser.resolveMutableUserId(session, userId);
        return collectService.deleteSongListCollect(targetUserId, songListId);
    }

    // 是否收藏歌曲
    @PostMapping("/collection/status")
    public R<?> isCollection(@RequestBody CollectRequest isCollectRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return collectService.existSongId(isCollectRequest, currentUserId);

    }

    // 是否收藏歌单
    @PostMapping("/collection/songList/status")
    public R<?> isSongListCollection(@RequestBody CollectRequest isCollectRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return collectService.existSongListId(isCollectRequest, currentUserId);
    }

    // 返回的指定用户 ID 收藏的列表（本人或管理员）
    @GetMapping("/collection/detail")
    public R<?> collectionOfUser(@RequestParam Integer userId, HttpSession session) {
        if (SessionUser.isAdmin(session)) {
            return collectService.collectionOfUser(userId);
        }
        Integer currentUserId = SessionUser.requireUserId(session);
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("无权查看他人收藏");
        }
        return collectService.collectionOfUser(currentUserId);
    }

    // 返回收藏指定歌单的用户列表和总数
    @GetMapping("/collection/songList/collectors")
    public R<?> collectionUsersOfSongList(@RequestParam Integer songListId) {
        return collectService.collectionUsersOfSongList(songListId);
    }
}
