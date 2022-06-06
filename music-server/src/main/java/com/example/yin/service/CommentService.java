package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.request.CommentRequest;

public interface CommentService {

    R addComment(CommentRequest addCommentRequest);

    R updateCommentMsg(CommentRequest upCommentRequest);

    R deleteComment(Integer id);

    R commentOfSongId(Integer songId);

    R commentOfSongListId(Integer songListId);

}
