package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.domain.Comment;
import com.example.yin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;


    // 提交评论
    @PostMapping("/comment/add")
    public R addComment(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        String type = req.getParameter("type");
        String songListId = req.getParameter("songListId");
        String songId = req.getParameter("songId");
        String content = req.getParameter("content").trim();

        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if (new Byte(type) == 0) {
            comment.setSongId(Integer.parseInt(songId));
        } else if (new Byte(type) == 1) {
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        comment.setCreateTime(new Date());
        boolean res = commentService.addComment(comment);
        if (res) {
            return R.success("评论成功");
        } else {
            return R.error("评论失败");
        }
    }

    // 删除评论
    @GetMapping("/comment/delete")
    public R deleteComment(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = commentService.deleteComment(Integer.parseInt(id));
        if (res) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 获得指定歌曲 ID 的评论列表
    @GetMapping("/comment/song/detail")
    public R commentOfSongId(HttpServletRequest req) {
        String songId = req.getParameter("songId");

        return R.success(null, commentService.commentOfSongId(Integer.parseInt(songId)));
    }

    // 获得指定歌单 ID 的评论列表
    @GetMapping("/comment/songList/detail")
    public Object commentOfSongListId(HttpServletRequest req) {
        String songListId = req.getParameter("songListId");

        return R.success(null, commentService.commentOfSongListId(Integer.parseInt(songListId)));
    }

    // 点赞
    @PostMapping("/comment/like")
    public R commentOfLike(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String up = req.getParameter("up").trim();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUp(Integer.parseInt(up));

        boolean res = commentService.updateCommentMsg(comment);
        if (res) {
            return R.success("点赞成功");
        } else {
            return R.error("点赞失败");
        }
    }
}
