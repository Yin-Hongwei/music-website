package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.yin.music.model.R;
import com.yin.music.model.request.CommentRequest;
import com.yin.music.service.CommentService;
import com.yin.music.support.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;


    // 提交评论
    @PostMapping("/comment/add")
    public R<?> addComment(@RequestBody CommentRequest addCommentRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return commentService.addComment(addCommentRequest, currentUserId);
    }

    // 删除评论 — app user: own comments only; admin (manage): any comment
    @DeleteMapping("/comment/delete")
    public R<?> deleteComment(@RequestParam Integer id, HttpSession session) {
        SessionUser.requireAppUserOrAdmin(session);
        Integer currentUserId = SessionUser.getUserId(session);
        boolean asAdmin = SessionUser.isAdmin(session);
        return commentService.deleteComment(id, currentUserId, asAdmin);
    }

    // 获得指定歌曲 ID 的评论列表
    @GetMapping("/comment/song/detail")
    public R<?> commentOfSongId(@RequestParam Integer songId) {
        return commentService.commentOfSongId(songId);
    }

    // 获得指定歌单 ID 的评论列表
    @GetMapping("/comment/songList/detail")
    public R<?> commentOfSongListId(@RequestParam Integer songListId) {
        return commentService.commentOfSongListId(songListId);
    }
}
