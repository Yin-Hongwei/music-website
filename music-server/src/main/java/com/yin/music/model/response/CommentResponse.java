package com.yin.music.model.response;

import lombok.Data;

import java.util.Date;

import com.yin.music.model.domain.AppUser;
import com.yin.music.model.domain.Comment;

@Data
public class CommentResponse {

    private static final String DELETED_USERNAME = "已注销";

    private Integer id;
    private Integer userId;
    private Integer songId;
    private Integer songListId;
    private String content;
    private Date createTime;
    private Byte type;
    private Integer likeCount;
    private String username;
    private String avatar;
    /** 评论用户是否已注销（用户记录不存在） */
    private Boolean userDeleted;

    public static CommentResponse from(Comment comment, AppUser user) {
        CommentResponse target = new CommentResponse();
        target.setId(comment.getId());
        target.setUserId(comment.getUserId());
        target.setSongId(comment.getSongId());
        target.setSongListId(comment.getSongListId());
        target.setContent(comment.getContent());
        target.setCreateTime(comment.getCreateTime());
        target.setType(comment.getType());
        target.setLikeCount(comment.getLikeCount());
        boolean deleted = user == null;
        target.setUserDeleted(deleted);
        target.setUsername(deleted ? DELETED_USERNAME : user.getUsername());
        target.setAvatar(deleted ? "" : user.getAvatar());
        return target;
    }
}
