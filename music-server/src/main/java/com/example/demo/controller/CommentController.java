package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Comment;
import com.example.demo.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@Controller
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

//  提交评论
    @ResponseBody
    @RequestMapping(value = "/comment/add", method = RequestMethod.POST)
    public Object addComment(HttpServletRequest req){

        JSONObject jsonObject = new JSONObject();
        String user_id = req.getParameter("userId");
        String type = req.getParameter("type");
        String song_list_id=req.getParameter("songListId");
        String song_id=req.getParameter("songId");
        String content = req.getParameter("content").trim();

        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(user_id));
        comment.setType(new Byte(type));
        if (new Byte(type) == 0) {
            comment.setSongId(Integer.parseInt(song_id));
        } else if (new Byte(type) == 1) {
            comment.setSongListId(Integer.parseInt(song_list_id));
        }
        comment.setContent(content);
        comment.setCreateTime(new Date());
        boolean res = commentService.addComment(comment);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "评论成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "评论失败");
            return jsonObject;
        }
    }

//    获取所有评论列表
    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public Object allComment(){
        return commentService.allComment();
    }

//    获得指定歌曲ID的评论列表
    @RequestMapping(value = "/comment/song/detail", method = RequestMethod.GET)
    public Object commentOfSongId(HttpServletRequest req){
        String songId = req.getParameter("songId");
        return commentService.commentOfSongId(Integer.parseInt(songId));
    }

//    获得指定歌单ID的评论列表
    @RequestMapping(value = "/comment/songList/detail", method = RequestMethod.GET)
    public Object commentOfSongListId(HttpServletRequest req){
        String songListId = req.getParameter("songListId");
        return commentService.commentOfSongListId(Integer.parseInt(songListId));
    }

//    点赞
    @ResponseBody
    @RequestMapping(value = "/comment/like", method = RequestMethod.POST)
    public Object commentOfLike(HttpServletRequest req){

    JSONObject jsonObject = new JSONObject();
    String id = req.getParameter("id").trim();
    String up = req.getParameter("up").trim();

    Comment comment = new Comment();
    comment.setId(Integer.parseInt(id));
    comment.setUp(Integer.parseInt(up));
    boolean res = commentService.updateCommentMsg(comment);
    if (res){
        jsonObject.put("code", 1);
        jsonObject.put("msg", "点赞成功");
        return jsonObject;
    }else {
        jsonObject.put("code", 0);
        jsonObject.put("msg", "点赞失败");
        return jsonObject;
    }
}

//    删除评论
    @RequestMapping(value = "/comment/delete", method = RequestMethod.GET)
    public Object deleteComment(HttpServletRequest req){
        String id = req.getParameter("id");
        return commentService.deleteComment(Integer.parseInt(id));
    }

//    更新评论
    @ResponseBody
    @RequestMapping(value = "/comment/update", method = RequestMethod.POST)
    public Object updateCommentMsg(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String user_id = req.getParameter("userId").trim();
        String song_id = req.getParameter("songId").trim();
        String song_list_id = req.getParameter("songListId").trim();
        String content = req.getParameter("content").trim();
        String type = req.getParameter("type").trim();
        String up = req.getParameter("up").trim();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUserId(Integer.parseInt(user_id));
        if (song_id == "") {
            comment.setSongId(null);
        } else {
            comment.setSongId(Integer.parseInt(song_id));
        }

        if (song_list_id == "") {
            comment.setSongListId(null);
        } else {
            comment.setSongListId(Integer.parseInt(song_list_id));
        }
        comment.setContent(content);
        comment.setType(new Byte(type));
        comment.setUp(Integer.parseInt(up));

        boolean res = commentService.updateCommentMsg(comment);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }
}
