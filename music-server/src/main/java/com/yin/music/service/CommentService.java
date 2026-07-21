package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.Comment;
import com.yin.music.model.request.CommentRequest;

public interface CommentService extends IService<Comment> {

    R<?> addComment(CommentRequest addCommentRequest);

    R<?> updateCommentMsg(CommentRequest upCommentRequest);

    R<?> deleteComment(Integer id);

    R<?> commentOfSongId(Integer songId);

    R<?> commentOfSongListId(Integer songListId);

}
