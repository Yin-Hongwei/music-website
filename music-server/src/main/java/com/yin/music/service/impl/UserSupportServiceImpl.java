package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.mapper.CommentMapper;
import com.yin.music.mapper.UserSupportMapper;
import com.yin.music.model.domain.Comment;
import com.yin.music.model.domain.UserSupport;
import com.yin.music.model.request.UserSupportRequest;
import com.yin.music.service.UserSupportService;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserSupportServiceImpl extends ServiceImpl<UserSupportMapper, UserSupport>
        implements UserSupportService {

    private final UserSupportMapper userSupportMapper;
    private final CommentMapper commentMapper;

    @Override
    public R<?> isUserSupportComment(UserSupportRequest userSupportRequest, Integer currentUserId) {
        QueryWrapper<UserSupport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", userSupportRequest.getCommentId());
        queryWrapper.eq("user_id", currentUserId);
        if (userSupportMapper.selectCount(queryWrapper) > 0) {
            return R.success("已点赞", true);
        }
        return R.success("未点赞", false);
    }

    @Transactional
    @Override
    public R<?> insertCommentSupport(UserSupportRequest userSupportRequest, Integer currentUserId) {
        Integer commentId = userSupportRequest.getCommentId();
        if (commentId == null || commentMapper.selectById(commentId) == null) {
            return R.error("评论不存在");
        }
        QueryWrapper<UserSupport> exists = new QueryWrapper<>();
        exists.eq("comment_id", commentId);
        exists.eq("user_id", currentUserId);
        if (userSupportMapper.selectCount(exists) > 0) {
            return R.success("已点赞", Collections.singletonMap("likeCount", syncLikeCount(commentId)));
        }

        UserSupport userSupport = new UserSupport();
        userSupport.setCommentId(commentId);
        userSupport.setUserId(currentUserId);
        try {
            if (userSupportMapper.insert(userSupport) <= 0) {
                return R.error("点赞失败");
            }
        } catch (DuplicateKeyException e) {
            return R.success("已点赞", Collections.singletonMap("likeCount", syncLikeCount(commentId)));
        }
        return R.success("点赞成功", Collections.singletonMap("likeCount", syncLikeCount(commentId)));
    }

    @Transactional
    @Override
    public R<?> deleteCommentSupport(UserSupportRequest userSupportRequest, Integer currentUserId) {
        Integer commentId = userSupportRequest.getCommentId();
        if (commentId == null) {
            return R.error("参数错误");
        }
        QueryWrapper<UserSupport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        queryWrapper.eq("user_id", currentUserId);
        userSupportMapper.delete(queryWrapper);
        return R.success("已取消点赞", Collections.singletonMap("likeCount", syncLikeCount(commentId)));
    }

    /** Recount supports and persist onto comment.like_count. */
    private int syncLikeCount(Integer commentId) {
        QueryWrapper<UserSupport> countWrapper = new QueryWrapper<>();
        countWrapper.eq("comment_id", commentId);
        Long count = userSupportMapper.selectCount(countWrapper);
        int likeCount = count == null ? 0 : count.intValue();
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setLikeCount(likeCount);
        commentMapper.updateById(comment);
        return likeCount;
    }
}
