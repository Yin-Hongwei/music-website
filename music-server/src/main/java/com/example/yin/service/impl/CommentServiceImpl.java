package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.CommentMapper;
import com.example.yin.domain.Comment;
import com.example.yin.request.CommentRequest;
import com.example.yin.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public R addComment(CommentRequest addCommentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentRequest, comment);
        comment.setType(addCommentRequest.getNowType());
        comment.setCreateTime(new Date());
        if (commentMapper.insertSelective(comment) > 0) {
            return R.success("评论成功");
        } else {
            return R.error("评论失败");
        }
    }

    @Override
    public R updateCommentMsg(CommentRequest addCommentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentRequest,comment);
        if (commentMapper.updateCommentMsg(comment) > 0) {
            return R.success("点赞成功");
        } else {
            return R.error("点赞失败");
        }
    }

    //    删除评论
    @Override
    public R deleteComment(Integer id) {
        if (commentMapper.deleteComment(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R commentOfSongId(Integer songId) {
        return R.success(null, commentMapper.commentOfSongId(songId));
    }

    @Override
    public R commentOfSongListId(Integer songListId) {
        return R.success(null, commentMapper.commentOfSongListId(songListId));
    }
}
